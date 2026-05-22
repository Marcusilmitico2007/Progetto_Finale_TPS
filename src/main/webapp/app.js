const API_URL = "/Progetto_Finale/api/categories";

let currentEditId = null;

window.onload = () => {

    loadCategories();

    document
        .getElementById("category-form")
        .addEventListener("submit", e => {
            e.preventDefault();
            saveCategory();
        });

    document
        .getElementById("search-input")
        .addEventListener("input", searchCategories);

    document
        .getElementById("refresh-btn")
        .addEventListener("click", loadCategories);

    document
        .getElementById("reset-btn")
        .addEventListener("click", clearForm);
};


async function loadCategories() {

    try {

        const res = await fetch(API_URL);
        const data = await res.json();

        renderTable(data);

        document.getElementById("counter").innerText =
            `${data.length} categorie caricate`;

    } catch (err) {

        console.error(err);
        alert("Errore caricamento categorie");
    }
}


function renderTable(data) {

    const tbody =
        document.getElementById("category-table-body");

    tbody.innerHTML = "";

    data.forEach(item => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${item.categoryID}</td>
            <td>${item.categoryName}</td>
            <td>${item.description || ""}</td>
            <td>
                <button onclick="editCategory(${item.categoryID})">Modifica</button>
                <button onclick="deleteCategory(${item.categoryID})">Elimina</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}



async function saveCategory() {

    const name =
        document.getElementById("category-name").value;

    const desc =
        document.getElementById("category-description").value;

    if (!name) {
        alert("Nome obbligatorio");
        return;
    }

    const payload = {
        categoryName: name,
        description: desc
    };

    try {

        let res;

        if (currentEditId) {

            res = await fetch(`${API_URL}?id=${currentEditId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

        } else {

            res = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });
        }

        if (!res.ok) {
            const text = await res.text();
            throw new Error(text);
        }

        clearForm();
        loadCategories();

    } catch (err) {

        console.error(err);
        alert("Operazione non completata");
    }
}



async function editCategory(id) {

    try {

        const res = await fetch(`${API_URL}?id=${id}`);

        if (!res.ok) throw new Error("Errore GET");

        const item = await res.json();

        currentEditId = item.categoryID;

        document.getElementById("category-name").value =
            item.categoryName;

        document.getElementById("category-description").value =
            item.description;

        document.getElementById("form-title").innerText =
            `Modifica categoria #${item.categoryID}`;

        document.getElementById("cancel-edit-btn").classList.remove("hidden");

    } catch (err) {

        console.error(err);
        alert("Errore modifica");
    }
}


async function deleteCategory(id) {

    if (!confirm("Eliminare categoria?")) return;

    try {

        const res = await fetch(`${API_URL}?id=${id}`, {
            method: "DELETE"
        });

        if (!res.ok) throw new Error("Errore delete");

        loadCategories();

    } catch (err) {

        console.error(err);
        alert("Errore eliminazione");
    }
}



function clearForm() {

    currentEditId = null;

    document.getElementById("category-name").value = "";
    document.getElementById("category-description").value = "";

    document.getElementById("form-title").innerText = "Nuova categoria";

    document.getElementById("cancel-edit-btn").classList.add("hidden");
}


async function searchCategories() {

    const q =
        document.getElementById("search-input").value.toLowerCase();

    const res = await fetch(API_URL);
    const data = await res.json();

    const filtered = data.filter(x =>
        (x.categoryName || "").toLowerCase().includes(q) ||
        (x.description || "").toLowerCase().includes(q)
    );

    renderTable(filtered);
}