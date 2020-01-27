function pageLoad(){

    let savesHTML = '<table>' +
        '<tr>' +
        '<th>Save Name</th>' +
        '<th>Save Data</th>' +
        '<th class="last">Options</th>' +
        '</tr>'

        fetch('/saves/read', {method: 'get'}).then(response => response.json()).then( saves => {

            for(let read of saves){
                savesHTML += `<tr>` +
                    `<td>${read.name}</td>` +
                    `<td>${read.data}</td>` +
                    `<td class="last">` +
                    `<button class='editButton' data-id='${read.id}'>Edit</button>button` +
                    `<button class='deleteButton' data-id='${read.id}'>Delete</button>button` +
                    `</td>`
                    `</tr>`;
            }

            savesHTML += `</table>`;

            document.getElementById("listDiv").innerHTML = savesHTML;

            let editButtons = document.getElementsByClassName("editButton");
            for (let button of editButtons){
                button.addEventListener("click", editSaves);
            }

            let deleteButtons = document.getElementsByClassName("deleteButton");
            for (let button of deleteButtons){
                button.addEventListener("click", deleteSaves)
            }
            checkLogin();
    });

    document.getElementById("saveButton").addEventListener("click", saveEditSaves);
    document.getElementById("cancelButton").addEventListener("click", cancelEditSaves);
}

function editSaves(event){
    const id = event.target.getAttribute("data-id");

    if(id === null){
        document.getElementById("editHeading").innerHTML = 'Add new save:';

        document.getElementById("SaveID").value = '';
        document.getElementById("SaveName").value = '';

        document.getElementById("listDiv").style.display = 'none';
        document.getElementById("editDiv").style.display = 'block';
    } else {
        fetch('saves/read/' + id, {method: 'get'}).then(response => response.json()).then(saves =>{
            if(saves.hasOwnProperty('error')){
                alert(saves.error);
            } else {
                document.getElementById("editHeading").innerHTML = 'Editing ' + saves.name + ':';

                document.getElementById("SaveID").value = id;
                document.getElementById("SaveName").value = saves.name;

                document.getElementById("listDiv").style.display = 'none';
                document.getElementById("editDiv").style.display = 'block';
            }
        });
    }
}

function saveEditSaves(event){
    event.preventDefault();

    if(document.getElementById("name").value.trim() === ''){
        alert("Please provide a save name.");
        return;
    }

    if(document.getElementById("data").value.trim() === ''){
        alert("Please provide a save data.");
        return;
    }

    const id = document.getElementById("SaveID").value;
    const form = document.getElementById("SaveForm");
    const formData = new FormData(form);

    let apiPath = '';
    if (id === ''){
        apiPath = 'saves/create';
    } else {
        apiPath = 'saves/update';
    }

    fetch(apiPath, {method: 'post', body: formData}).then(response => response.json()).then(responseData => {
        if(responseData.hasOwnProperty('error')){
            alert(responseData.error);
        } else {
            document.getElementById("listDiv").style.display = 'block';
            document.getElementById("editDiv").style.display = 'none';
        }
    });
}

function cancelEditSaves(event){
    event.preventDefault();

    document.getElementById("listDiv").style.display = 'block';
    document.getElementById("editDiv").style.display = 'none';
}

function deleteSaves(event){
    const confirmation = confirm("Are you sure you want to delete this save?");

    if (confirmation === true){
        let id = event.target.getAttribute("data=id");
        let formData = new FormData();
        formData.apped("id", id);

        fetch('/saves/delete', {method: 'post', body: formData}).then(response => response.json()).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                pageLoad();
            }
        });
    }
}

function checkLogin() {
    let username = Cookies.get("username");
    let logInHTML = '';
    if (username === undefined) {
        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }
        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }
        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";
    } else {
        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "visible";
        }
        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "visible";
        }
        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";
    }
    document.getElementById("loggedInDetails").innerHTML = logInHTML;
}

