CorsConfiguration config = new CorsConfiguration();

config.setAllowedOriginPatterns(List.of(
        "http://localhost:5173",
        "https://*.vercel.app"
));

config.setAllowedMethods(List.of(
        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
));

config.setAllowedHeaders(List.of("*"));
config.setAllowCredentials(true);