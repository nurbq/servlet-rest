package lab.andersen.filter;

import lab.andersen.dao.UserDao;
import lab.andersen.dto.UserDto;
import lab.andersen.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter extends HttpFilter {

    private final UserService userService = new UserService(new UserDao());
    private static final Set<String> PUBLIC_PATH = Set.of("/Register", "/front/Register");


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestURI = ((HttpServletRequest) req).getRequestURI();

        if (isPublicPath(requestURI)) {
            chain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                StringTokenizer st = new StringTokenizer(authHeader);
                if (st.hasMoreTokens()) {
                    String basic = st.nextToken();
                    if (basic.equalsIgnoreCase("Basic")) {
                        String credentials = new String(Base64.getDecoder().decode(st.nextToken()));
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String username = credentials.substring(0, p).trim();
                            String password = credentials.substring(p + 1).trim();
                            Optional<UserDto> user = userService.login(username, password);
                            if (user.isPresent()) {
                                chain.doFilter(request, response);
                            } else {
                                unauthorized(response, "Invalid user credentials");
                            }
                        }
                    }
                }
            } else {
                unauthorized(response, "No user credentials");
            }
        }
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic=\"" + "Protected" + "\"");
        response.sendError(401, message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isPublicPath(String requestURI) {
        return PUBLIC_PATH.stream().anyMatch(requestURI::contains);
    }
}
