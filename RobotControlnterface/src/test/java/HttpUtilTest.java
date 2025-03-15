import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import de.sdrs.robotcontrolinterface.util.HttpUtil;
import org.junit.jupiter.api.Test;
import java.net.HttpCookie;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpUtilTest {

    @Test
    void shouldReturnSessionIdCookie() {
        HttpExchange exchange = mock(HttpExchange.class);
        HttpCookie cookie = new HttpCookie("sessionId", "testSessionId");
        Headers headers = new Headers();
        headers.add("Cookie", cookie.toString());
        when(exchange.getRequestHeaders()).thenReturn(headers);
        assertEquals(cookie, HttpUtil.getSessionIdCookie(exchange));
    }

    @Test
    void shouldReturnNullWhenNoSessionIdCookie() {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        when(exchange.getRequestHeaders()).thenReturn(headers);
        assertNull(HttpUtil.getSessionIdCookie(exchange));
    }
}