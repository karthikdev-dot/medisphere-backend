package healthcare.FHIRService;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import healthcare.DTO.LoginRequestDTO;
import healthcare.DTO.LoginResponseDTO;
import healthcare.Entity.User;
import healthcare.Repository.RegisterRepo;
import healthcare.jwt.JWT;

@Service
public class LoginService{

    @Autowired
    private RegisterRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWT jwtgenerator;

    // LOGIN
    public ResponseEntity<LoginResponseDTO> login(
            LoginRequestDTO loginreq) {

        // 1. Find user in MongoDB
        Optional<User> user = userRepository
                .findByUsername(loginreq.getUsername());
        
        if(user.isEmpty()) {
        	
         throw	new RuntimeException("user not found");
        }
        
        User data=user.get();
        
		if(!passwordEncoder.matches(loginreq.getPassword(),data.getPassword())) {
			
			throw new RuntimeException("invalid password");
		}
		


        // 3. Generate access token
        String accessToken =
                jwtgenerator.generateAccessToken(
                        data.getUsername()
                );


        // 4. Generate refresh token
        String refreshToken =
                jwtgenerator.generateRefreshToken(
                        data.getUsername()
                );


        // 5. Create response DTO
        LoginResponseDTO response =
                new LoginResponseDTO();


        response.setAccessToken(
                accessToken
        );

        response.setRefreshToken(
                refreshToken
        );

        response.setUsername(
                data.getUsername()
        );

        response.setRole(
                data.getRole()
        );


        // 6. Return response
    	return ResponseEntity.ok(response);
    }
}

