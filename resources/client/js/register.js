function pageLoad() {
    if(window.location.search === '?logout') {
        document.getElementById('content').innerHTML = '<h1>Logging out. Please wait...</h1>';
        logout();
    } else {
        document.getElementById("registerButton").addEventListener("click", editUser);
    }
}

function editUser(event) {
    const id = event.target.getAttribute("data-id");

    if (id === null) {
        document.getElementById("editHeading").innerHTML = 'Create New User:';

        document.getElementById("UserID").value = '';
        document.getElementById("UserName").value = '';
        document.getElementById("UserEmail").value = '';
        document.getElementById("UserPass").value = '';

        document.getElementById("listDiv").style.display = 'none';
        document.getElementById("editDiv").style.display = 'block';
    } else {

        fetch('/users/read/' + id, {method: 'get'}
        ).then(response => response.json()
        ).then(user => {
            if (user.hasOwnProperty('error')) {
                alert(user.error);
            } else {
                document.getElementById("editHeading").innerHTML = 'Editing ' + user.Username + ':';

                document.getElementById("UserID").value = id;
                document.getElementById("UserName").value = user.Username;
                document.getElementById("UserEmail").value = user.Useremail;
                document.getElementById("UserPass").value = user.Userpass;

                document.getElementById("listDiv").style.display = 'none';
                document.getElementById("editDiv").style.display = 'block';
            }
        });
    }
}

function saveEditUser(event) {
    event.preventDefault();
    if (document.getElementById("userName").value.trim() === '') {
        alert("Please provide a username.");
        return;
    }
    if (document.getElementById("userEmail").value.trim() === '') {
        alert("Please provide an email for the user.");
        return;
    }
    if (document.getElementById("userPass").value.trim() === '') {
        alert("Please provide a password for the user.");
        return;
    }

    const id = document.getElementById("userId").value;
    const form = document.getElementById("userForm");
    const formData = new FormData(form);

    let apiPath = '';
    if (id === '') {
        apiPath = '/users/create';
    } else {
        apiPath = '/users/update';
    }

    fetch(apiPath, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            document.getElementById("listDiv").style.display = 'block';
            document.getElementById("editDiv").style.display = 'none';
            pageLoad();
        }
    });
}

function cancelEditUser(event) {
    event.preventDefault();
    document.getElementById("listDiv").style.display = 'block';
    document.getElementById("editDiv").style.display = 'none';
}

function deleteUser(event) {
    const ok = confirm("Are you sure you want to delete this user?");

    if (ok === true) {
        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("id", id);

        fetch('/users/delete', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
    }
}


