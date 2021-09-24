package ru.gb.antonov.j67.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j67.beans.errorhandlers.ErrorMessage;
import ru.gb.antonov.j67.beans.errorhandlers.OurValidationException;
import ru.gb.antonov.j67.beans.services.OurUserDetailsService;
import ru.gb.antonov.j67.beans.utils.AppLoggingAspect;
import ru.gb.antonov.j67.beans.utils.JwtokenUtil;
import ru.gb.antonov.j67.entities.OurUser;
import ru.gb.antonov.j67.entities.dtos.AopStatisticDto;
import ru.gb.antonov.j67.entities.dtos.AuthRequest;
import ru.gb.antonov.j67.entities.dtos.AuthResponse;
import ru.gb.antonov.j67.entities.dtos.RegisterRequest;

import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping ("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController
{
    private final OurUserDetailsService ourUserDetailsService;
    private final JwtokenUtil           jwtokenUtil;
    private final AuthenticationManager authenticationManager;
    private final AppLoggingAspect appLoggingAspect;


    //http://localhost:8189/market/api/v1/auth/login        index10.js,
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser (@RequestBody AuthRequest authRequest)
    {
        String login = authRequest.getLogin();
        String password = authRequest.getPassword();

        try
        {   UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken (
                                                                login, password);
            authenticationManager.authenticate (upat);
        }
        catch (BadCredentialsException e)
        {
            String errMsg = String.format ("Incorrect login (%s) or password (%s).", login, password);
            return new ResponseEntity<> (new ErrorMessage (errMsg), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){e.printStackTrace();}

        UserDetails userDetails = ourUserDetailsService.loadUserByUsername (login);
        String token = jwtokenUtil.generateJWToken (userDetails);

        return ResponseEntity.ok (new AuthResponse (token));
    }

    //http://localhost:8189/market/api/v1/auth/register     registration.js,
    @PostMapping ("/register")
    public ResponseEntity<?> registerNewUser (@RequestBody @Validated RegisterRequest registerRequest,
                                              BindingResult br)
    {   if (br.hasErrors())
        {
            throw new OurValidationException (br.getAllErrors()
                                                .stream()
                                                .map (ObjectError::getDefaultMessage)
                                                .collect (Collectors.toList ()));
        }
        String login    = registerRequest.getLogin ();
        String password = registerRequest.getPassword ();
        String email    = registerRequest.getEmail ();

        Optional<OurUser> optionalOurUser = ourUserDetailsService.createNewOurUser (
                                                    login, password, email);
        if (optionalOurUser.isPresent())
        {
            try
            {   authenticationManager.authenticate (new UsernamePasswordAuthenticationToken (
                                                        login, password));
            }
            catch (BadCredentialsException e)
            {
                String errMsg = String.format ("Incorrect login (%s) or password (%s).", login, password);
                return new ResponseEntity<> (new ErrorMessage (errMsg), HttpStatus.UNAUTHORIZED);
            }
            catch (Exception e){e.printStackTrace();}

            UserDetails userDetails = ourUserDetailsService.loadUserByUsername (login);
            String token = jwtokenUtil.generateJWToken (userDetails);

            return ResponseEntity.ok (new AuthResponse (token));
        }
        return new ResponseEntity<> (new ErrorMessage ("Something went wrong."),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //http://localhost:8189/market/api/v1/auth/statistics   index10.js,
    @GetMapping ("/statistics")
    public AopStatisticDto getAopStatistics ()
    {
        return appLoggingAspect.getStatisticsAsDto();
        /* Конечно AuthController не самый подходящий контроллер для такого запроса, но
           он самый малозагруженный, а создавать отдельный контроллер для единственного
           вызова показалось излишним. */
    }
}
