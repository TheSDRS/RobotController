import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import de.sdrs.robotcontrolinterface.client.WebClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebClientTest {

    @Test
    void shouldReturnPasswordHash() {
        WebClient webClient = new WebClient("hashedPassword");
        assertEquals("hashedPassword", webClient.getPasswordHash());
    }

    @Test
    void shouldCheckAuthWithInvalidSession() {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        headers.add("Cookie", "sessionId=invalidSessionId");
        when(exchange.getRequestHeaders()).thenReturn(headers);
        assertFalse(WebClient.CheckAuth(exchange));
    }
}