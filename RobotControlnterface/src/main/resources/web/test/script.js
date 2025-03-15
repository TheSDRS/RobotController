document.addEventListener("DOMContentLoaded", function() {
    const urls = [
        '/api/nodes/start',
        '/api/nodes/stop',
        '/api/nodes/pause',
        '/api/direct/move',
        '/api/direct/part/motor',
        '/api/direct/part/hub/light',
        '/api/direct/part/hub/speaker',
        '/api/direct/part/hub/screen',
        '/api/direct/part/hub/battery',
        '/api/direct/sensors/touch',
        '/api/direct/sensors/color',
        '/api/direct/sensors/ultrasonic'
    ];

    function testApi(url, data = null) {
        $.get(url, data, function(response, status, xhr) {
            const responseContainer = document.getElementById("responseContainer");
            let responseRow = document.querySelector(`tr[data-url="${url}"]`);
            const newContent = `<td>${url}</td><td><strong>Status:</strong> ${xhr.status} <strong>Response:</strong> ${response}</td>`;

            if (responseRow) {
                if (responseRow.innerHTML !== newContent) {
                    responseRow.innerHTML = newContent;
                }
            } else {
                responseRow = document.createElement("tr");
                responseRow.setAttribute("data-url", url);
                responseRow.innerHTML = newContent;
                responseContainer.querySelector("tbody").appendChild(responseRow);
            }
        });
    }

    function testAllApis() {
        document.getElementById("responseContainer").innerHTML = "<table><thead><tr><th>URL</th><th>Response</th></tr></thead><tbody></tbody></table>";
        const tbody = document.querySelector("#responseContainer tbody");
        urls.forEach(url => testApi(url, { exampleKey: "exampleValue" }));
    }

    function sendSpecificRequest() {
        const specificUrl = '/api/specific/endpoint';
        const data = { key: 'value' };
        testApi(specificUrl, data);
    }

    let countdown = 10;
    let time = 0;
    let isPaused = false;

    function updateTimerAndLoadingBar() {
        if (!isPaused) {
            const timerDiv = document.getElementById("timer");
            const loadingBar = document.getElementById("loadingBar");

            timerDiv.textContent = `Next request in: ${countdown} seconds`;
            loadingBar.style.width = `${(time / 10000) * 100}%`;

            time += 10;
            if (time >= 10000) {
                time = 0;
                countdown = 11;
                testAllApis();
            }
            if (time % 1000 === 0) {
                countdown--;
            }
        }
    }

    document.getElementById("pauseButton").addEventListener("click", function() {
        isPaused = !isPaused;
        this.textContent = isPaused ? "Resume" : "Pause";
    });

    document.getElementById("specificRequestButton").addEventListener("click", sendSpecificRequest);

    $(document).ready(function(){
        $("#registerBtn").click(function(event){
            event.preventDefault(); // Prevent the default form submission

            var username = "admin";
            var password = "password";

            $.ajax({
                url: '/api/register',
                type: 'POST',
                data: {
                    username: username,
                    password: password
                },
                success: function(response) {
                    alert('Login successful');
                    // Handle the response as needed
                },
                error: function(xhr, status, error) {
                    alert('Login failed: ' + xhr.responseText);
                    // Handle the error as needed
                }
            });
        });
    });

    testAllApis();
    setInterval(updateTimerAndLoadingBar, 10);
});