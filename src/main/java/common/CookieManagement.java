package common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManagement {
    public static int days = 1;
	
	public static boolean setCookie(HttpServletResponse response, String key, String value) {

		if (value != null) {
			Cookie newCookie = new Cookie(key, value);
			newCookie.setMaxAge(days * 24 * 60 * 60);
			newCookie.setPath("/");
			System.out.println("setCokkie");
			response.addCookie(newCookie);

			return true;
		}

		return false;
	}
	
	public static String getCookie(HttpServletRequest request, String key) {

		Cookie[] cookies = request.getCookies();
		
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie: cookies) {
				
				if (cookie.getName().equals(key)) {

					String value = cookie.getValue().trim();

					return value;
				}
			}
		}
		return "";
	}

	public static void removeCookie(HttpServletResponse response, String key) {

		Cookie newCookie = new Cookie(key, "");
		newCookie.setPath("/");
		

		response.addCookie(newCookie);
	}
}
