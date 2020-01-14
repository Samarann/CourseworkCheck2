function pageLoad() {

    let saveHTML = `<table>` +
        '<tr>' +
        '<th>Name</th>' +
        '<th>Owner</th>' +
        '<th class="last">Options</th>' +
        '</tr>';

    fetch('/client/main', {method: 'get'}
    ).then(response => response.json()
    ).then(saves => {

        let myHTML = `<table>` +
            '<tr>' +
            '<th>Name</th>' +
            '<th>Owner</th>' +
            '<th class="last">Options</th>' +
            '</tr>';

        for (let save of saves) {

            myHTML += `<tr>` +
                `<td>${save.name}</td>` +
                `<td>${save.owner}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${save.name}'>Edit</button>` +
                `<button class='deleteButton' data-id='${save.name}'>Delete</button>` +
                `</td>` +
                `</tr>`;
        }

        saveHTML += '</table>';
        document.getElementById("listDiv").innerHTML = saveHTML;

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editSaves);
        }
        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteSaves);
        }
    });
    document.getElementById("saveButton").addEventListener("click", saveEditSaves);
    document.getElementById("cancelButton").addEventListener("click", cancelEditSaves);
}
function editSaves(event) {
    const id = event.target.getAttribute("data-id");
    if (id === null) {
        document.getElementById("editHeading").innerHTML = 'Add new Save:';
        document.getElementById("fruitId").value = '';
        document.getElementById("saveName").value = '';
        document.getElementById("listDiv").style.display = 'none';
        document.getElementById("editDiv").style.display = 'block';
    } else {
        fetch('/fruit/get/' + id, {method: 'get'}
        ).then(response => response.json()
        ).then(fruit => {
            if (fruit.hasOwnProperty('error')) {
                alert(fruit.error);
            } else {
                document.getElementById("editHeading").innerHTML = 'Editing ' + fruit.name + ':';
                document.getElementById("fruitId").value = id;
                document.getElementById("fruitName").value = fruit.name;
                document.getElementById("fruitImage").value = fruit.image;
                document.getElementById("fruitColour").value = fruit.colour;
                document.getElementById("fruitSize").value = fruit.size;
                document.getElementById("listDiv").style.display = 'none';
                document.getElementById("editDiv").style.display = 'block';

            }
        });
    }
}
function saveEditFruit(event) {
    event.preventDefault();
    if (document.getElementById("fruitName").value.trim() === '') {
        alert("Please provide a fruit name.");
        return;
    }
    if (document.getElementById("fruitImage").value.trim() === '') {
        alert("Please provide a fruit image.");
        return;
    }
    if (document.getElementById("fruitColour").value.trim() === '') {
        alert("Please provide a fruit colour.");
        return;
    }
    if (document.getElementById("fruitSize").value.trim() === '') {
        alert("Please provide a fruit size.");
        return;
    }
    const id = document.getElementById("fruitId").value;
    const form = document.getElementById("fruitForm");
    const formData = new FormData(form);
    let apiPath = '';
    if (id === '') {
        apiPath = '/fruit/new';
    } else {
        apiPath = '/fruit/update';
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
function cancelEditFruit(event) {
    event.preventDefault();
    document.getElementById("listDiv").style.display = 'block';
    document.getElementById("editDiv").style.display = 'none';
}
function deleteFruit(event) {
    const ok = confirm("Are you sure?");
    if (ok === true) {
        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("id", id);
        fetch('/fruit/delete', {method: 'post', body: formData}
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