    package kz.adem.userservice.security;

    import jakarta.annotation.Nonnull;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import kz.adem.userservice.exception.UnauthorizedAccessException;
    import kz.adem.userservice.repository.TokenRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.util.StringUtils;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    @RequiredArgsConstructor
    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtTokenProvider jwtTokenProvider;
        private final UserDetailsService userDetailsService;
        private final TokenRepository tokenRepository;

        private String getTokenFromRequest(HttpServletRequest request) {
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }
        @Override
        protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                        @Nonnull HttpServletResponse response,
                                        @Nonnull FilterChain filterChain) throws ServletException, IOException {
            //get JWT token from HTTP request
            String token = getTokenFromRequest(request);
            //validate token
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                //get username from token
                String username = jwtTokenProvider.extractUsername(token);
                //load the user associated with token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String tokenType = jwtTokenProvider.extractTokenType(token);
                if ("refresh_token".equals(tokenType) && !request.getRequestURI().equals("/api/auth/refresh-token")) {
                    throw new UnauthorizedAccessException("Refresh token is not allowed");
                }
                // If it's a refresh token and the path is /api/auth/refresh-token, skip the isTokenValid check
                if (!("refresh_token".equals(tokenType) && request.getRequestURI().equals("/api/auth/refresh-token"))) {
                    boolean isTokenValid = tokenRepository.findByToken(token)
                            .map(t -> !t.isExpired() && !t.isRevoked())
                            .orElse(false);
                    if (!jwtTokenProvider.isTokenValid(token, userDetails) || !isTokenValid) {
                        throw new UnauthorizedAccessException("Token is not valid");
                    }
                }
                //create authentication object (UsernamePasswordAuthenticationToken)
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }

    }

//        @Override
//        protected void doFilterInternal(@Nonnull HttpServletRequest request,
//                                        @Nonnull HttpServletResponse response,
//                                        @Nonnull FilterChain filterChain) throws ServletException, IOException {
//            //get JWT token from HTTP request
//            String token = getTokenFromRequest(request);
//            //validate token
//            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
//                //get username from token
//                String username = jwtTokenProvider.extractUsername(token);
//                //load the user associated with token
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                String tokenType = jwtTokenProvider.extractTokenType(token);
//                if ("refresh_token".equals(tokenType) && !request.getRequestURI().equals("/api/auth/refresh-token")) {
//                    throw new RuntimeException("Refresh token is not allowed");
//                }
//                //check if token is valid
//                boolean isTokenValid = tokenRepository.findByToken(token)
//                        .map(t -> !t.isExpired() && !t.isRevoked())
//                        .orElse(false);
//
//                if (jwtTokenProvider.isTokenValid(token, userDetails) && isTokenValid) {
//                    //create authentication object (UsernamePasswordAuthenticationToken)
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//            }
//            filterChain.doFilter(request,response);
//
//
//        }