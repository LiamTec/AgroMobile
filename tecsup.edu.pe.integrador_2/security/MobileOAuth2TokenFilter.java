private boolean isMobileRequest(String userAgent, String requestPath) {
    // Aplica el filtro a TODAS las rutas /api/ (no solo /api/mobile/)
    return (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")))
            || (requestPath != null && requestPath.startsWith("/api/"));
}

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String userAgent = httpRequest.getHeader("User-Agent");
    String authorizationHeader = httpRequest.getHeader("Authorization");
    String requestPath = httpRequest.getRequestURI();

    // Solo procesar peticiones de móviles (por header o path)
    if (isMobileRequest(userAgent, requestPath)) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remover "Bearer "
            try {
                GoogleIdToken idToken = verifyGoogleToken(token);
                if (idToken != null) {
                    String googleId = idToken.getPayload().getSubject();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    googleId, // <--- AQUÍ el googleId real
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Token inválido: rechaza la request
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                    return;
                }
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error verificando token");
                return;
            }
        } else {
            // No hay token: rechaza la request
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falta token");
            return;
        }
    }
    chain.doFilter(request, response);
}

private GoogleIdToken verifyGoogleToken(String idTokenString) {
    try {
        if (googleClientId == null || googleClientId.isEmpty()) {
            return null;
        }
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JSON_FACTORY)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        return verifier.verify(idTokenString);
    } catch (Exception e) {
        System.err.println("Error verificando token de Google: " + e.getMessage());
    }
    return null;
} 