function parseCookieData(responseText) {
    const sessionIdMatch = responseText.match(/sessionId='([^']+)'/);
    const pathMatch = responseText.match(/path='([^']+)'/);
    const domainMatch = responseText.match(/domain='([^']+)'/);

    if (!sessionIdMatch || !pathMatch || !domainMatch) {
        return null;
    }

    return {
        sessionId: sessionIdMatch[1],
        path: pathMatch[1],
        domain: domainMatch[1]
    };
}

function setCookie(cookieData) {
    const { sessionId, path, domain } = cookieData;
    const cookieString = `sessionId=${sessionId}; path=${path}; domain=${domain}; SameSite=Lax`;
    document.cookie = cookieString;
}

function register() {
    var username = "WebClient";
    var password = $("#password").val();

    console.log(username, password);

    $.ajax({
        url: '/api/register',
        method: 'POST',
        data: {
            username: username,
            password: password
        },
        xhrFields: {
            withCredentials: true
        },
        success: function(response) {
            const cookieData = parseCookieData(response);
            if (cookieData) {
                setCookie(cookieData);
                window.location.href = '/control';
            } else {
                console.error('Failed to parse cookie data from response');
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr, status, error);
            if (xhr.readyState === 4) {
                clearTimeout(timer);
                if (xhr.status === 200) {
                    console.log('Server responded successfully.');
                } else {
                    removeCookie(cookieName);
                    console.log(`Server responded with status ${xhr.status}. Cookie "${cookieName}" removed.`);
                }
            }
            alert('Login failed: ' + xhr.responseText);
            // Handle the error as needed
        }
    });
}

function enteredInput(event) {
    if (event.key === "Enter") {
        register();
    }
}

$(document).ready(function() {
    document.getElementById("password").addEventListener("keydown", function (event) {
        enteredInput(event);
    });
});