@Bean
public CorsFilter corsFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedOrigin("http://localhost:8080");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource ub = new UrlBasedCorsConfigurationSource();
    ub.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(ub);
}