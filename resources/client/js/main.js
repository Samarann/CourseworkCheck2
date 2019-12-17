function pageLoad(){

    let myHTML = '<div style="text-align: center;">' +
        '<h1>Welcome to my API powered website!</h1>' +
        '<img src="/client/img/logo.png" alt="Logo"/>' +
        '<div style="front-style: italic;">' +
        'Generated at ' + now.toLocaleTimeString() +
        '</div>' +
        '</div>';

    document.getElementById("testDiv").innerHTML = myHTML;

        let HTML = '<table>' +
        '<tr>' +
        '<th>ID</th>' +
        '<th>Name</th>' +
        '</tr>';

        fetch('/client/main', {method: 'get'}).then(response => response.json()).then

    if(window.location.search === '?logout') {
        document.getElementById('content').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        document.getElementById("loginButton").addEventListener("click", login);
    }

    function login(event) {

        event.preventDefault();

        const form = document.getElementById("loginForm");
        const formData = new FormData(form);

        fetch("/users/login", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                Cookies.set("username", responseData.username);
                Cookies.set("token", responseData.token);

                window.location.href = '../main.html';
            }
        });
    }

    fetch("/users/logout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("username");
            Cookies.remove("token");

            window.location.href = '../main.html';

        }
    });


    //let now = new Date();

    //let myHTML = '<div style="text-align: center;">' +
    //    '<h1>Welcome to my API powered website!</h1>' +
    //    '<img src="/client/img/logo.png" alt="Logo"/>' +
    //    '<div style="front-style: italic;">' +
    //    'Generated at ' + now.toLocaleTimeString() +
    //    '</div>' +
    //    '</div>';

   //     document.getElementById("testDiv").innerHTML = myHTML;
}