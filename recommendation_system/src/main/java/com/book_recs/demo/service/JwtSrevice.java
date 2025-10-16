package main.java.com.book_recs.demo.service;

import main.java.com.book_recs.demo.model.User;

@Service
public class JwtSrevice {
    @Value("security.jwt.secret-key")
    private String secretKey;

    @Value("security.jwt.expiration-ms")
    private Long jwtExpirationMs; // 3600ms from application properties

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(userDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    //genmerate the JWT token
    public String gnerateToken(Map<String, Object> extraClaims,UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }


    //build the JWT token
    private String buildToken(Map<String, object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
