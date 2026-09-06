package healthcare.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWT {

   

    private final String secret =
            "hellomynameiskarthiktodayimdoinghealthcarefhir123456789";

    private final SignatureAlgorithm signatureAlgorithm =
            SignatureAlgorithm.HS256;

    private final byte[] secretBytes =
            secret.getBytes(StandardCharsets.UTF_8);

    private final SecretKey key =
            new SecretKeySpec(
                    secretBytes,
                    signatureAlgorithm.getJcaName()
            );


    private final Long accessTokenExpiration =
            15 * 60 * 1000L;

    private final Long refreshTokenExpiration =
            1000L * 60 * 60 * 24 * 7;

    public String generateAccessToken(
            String username) {

        return generateAccessToken(
                username,
                accessTokenExpiration
        );
    }


    // =====================================================
    // GENERATE REFRESH TOKEN
    // =====================================================

    public String generateRefreshToken(
            String username) {

        return generateRefreshToken(
                username,
                refreshTokenExpiration
        );
    }


    // =====================================================
    // ACTUAL ACCESS TOKEN GENERATION
    // =====================================================

    public String generateAccessToken(
            String username,
            Long expiration) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(
                        new Date(
                                System.currentTimeMillis()
                        )
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + expiration
                        )
                )

                .claim(
                        "role",
                        "USER"
                )

                // SMART-on-FHIR style scopes
                .claim(
                        "scope",
                        "user/Patient.read user/Observation.read"
                )

                .signWith(
                        key,
                        signatureAlgorithm
                )

                .compact();
    }


    // =====================================================
    // ACTUAL REFRESH TOKEN GENERATION
    // =====================================================

    public String generateRefreshToken(
            String username,
            Long expiration) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(
                        new Date(
                                System.currentTimeMillis()
                        )
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + expiration
                        )
                )

                .claim(
                        "type",
                        "refresh"
                )

                .signWith(
                        key,
                        signatureAlgorithm
                )

                .compact();
    }


    // =====================================================
    // EXTRACT USERNAME
    // =====================================================

    public String extractUsername(
            String token) {

        return Jwts.parserBuilder()

                .setSigningKey(key)

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();
    }


    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        try {

            final String username =
                    extractUsername(token);

            return username.equals(
                    userDetails.getUsername()
            )
            &&
            !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }


    // =====================================================
    // CHECK TOKEN EXPIRATION
    // =====================================================

    private boolean isTokenExpired(
            String token) {

        Date expiration =
                Jwts.parserBuilder()

                        .setSigningKey(key)

                        .build()

                        .parseClaimsJws(token)

                        .getBody()

                        .getExpiration();

        return expiration.before(
                new Date()
        );
    }
}