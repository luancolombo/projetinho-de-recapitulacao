const idInput = document.getElementById("person-id");
const firstNameInput = document.getElementById("first-name");
const lastNameInput = document.getElementById("last-name");
const addressInput = document.getElementById("address");
const genderInput = document.getElementById("gender");
const endpointAction = document.getElementById("endpoint-action");
const sendRequestButton = document.getElementById("send-request");
const searchAction = document.getElementById("search-action");
const searchRecordButton = document.getElementById("search-record");
const endpointHelp = document.getElementById("endpoint-help");
const responsePanel = document.getElementById("response-panel");
const responseMeta = document.getElementById("response-meta");
const responseBody = document.getElementById("response-body");
let updateLoaded = false;

const endpointHelpText = {
    findAll: "GET /person lista todas as pessoas mockadas pelo servico.",
    findById: "GET /person/{id} precisa apenas do campo ID.",
    create: "POST /person envia os dados preenchidos no formulario em JSON.",
    update: "PUT /person pede um ID, usa Search para carregar os dados e depois envia a atualizacao.",
    delete: "DELETE /person/{id} precisa apenas do campo ID."
};

function personPayload(includeId = true) {
    const payload = {
        firstName: firstNameInput.value.trim(),
        lastName: lastNameInput.value.trim(),
        address: addressInput.value.trim(),
        gender: genderInput.value
    };

    const id = idInput.value.trim();
    if (includeId && id) {
        payload.id = Number(id);
    }

    return payload;
}

function showResponse(method, endpoint, status, body, isError) {
    responsePanel.classList.toggle("error", isError);
    responseMeta.textContent = "Metodo: " + method + " | Endpoint: " + endpoint + " | Status: " + status;

    if (body === undefined || body === null || body === "") {
        responseBody.value = "Requisicao concluida sem corpo de resposta.";
        return;
    }

    if (typeof body === "string") {
        responseBody.value = body;
        return;
    }

    responseBody.value = JSON.stringify(body, null, 2);
}

function requireId(methodName) {
    if (!idInput.value.trim()) {
        showResponse(methodName, "/person/{id}", "-", "Informe um ID para esta operacao.", true);
        return false;
    }

    return true;
}

async function parseResponse(response) {
    const contentType = response.headers.get("content-type") || "";

    if (contentType.includes("application/json")) {
        return response.json();
    }

    return response.text();
}

async function callApi(action) {
    let method = "GET";
    let endpoint = "/person";
    const options = {};

    switch (action) {
        case "findAll":
            method = "GET";
            break;
        case "findById":
            if (!requireId("GET")) return;
            method = "GET";
            endpoint = "/person/" + encodeURIComponent(idInput.value.trim());
            break;
        case "create":
            method = "POST";
            idInput.value = "";
            options.headers = { "Content-Type": "application/json" };
            options.body = JSON.stringify({
                firstName: firstNameInput.value.trim(),
                lastName: lastNameInput.value.trim(),
                address: addressInput.value.trim(),
                gender: genderInput.value
            });
            break;
        case "update":
            if (!updateLoaded) {
                showResponse("PUT", "/person", "-", "Use o Search para carregar a pessoa antes de atualizar.", true);
                return;
            }
            method = "PUT";
            options.headers = { "Content-Type": "application/json" };
            options.body = JSON.stringify(personPayload());
            break;
        case "delete":
            if (!requireId("DELETE")) return;
            method = "DELETE";
            endpoint = "/person/" + encodeURIComponent(idInput.value.trim());
            break;
        default:
            showResponse("?", "/person", "-", "Acao desconhecida.", true);
            return;
    }

    options.method = method;

    try {
        const response = await fetch(endpoint, options);
        const body = await parseResponse(response);
        showResponse(method, endpoint, response.status, body, !response.ok);
    } catch (error) {
        showResponse(method, endpoint, "erro", "Nao foi possivel chamar a API.", true);
    }
}

function fillPersonForm(person) {
    idInput.value = person.id ?? "";
    firstNameInput.value = person.firstName ?? "";
    lastNameInput.value = person.lastName ?? "";
    addressInput.value = person.address ?? "";
    genderInput.value = person.gender ?? "";
}

async function searchRecordForUpdate() {
    if (endpointAction.value !== "update") {
        return;
    }

    if (!requireId("GET")) {
        return;
    }

    const endpoint = "/person/" + encodeURIComponent(idInput.value.trim());

    try {
        const response = await fetch(endpoint);
        const body = await parseResponse(response);

        if (!response.ok) {
            updateLoaded = false;
            updateFieldAvailability();
            showResponse("GET", endpoint, response.status, body, true);
            return;
        }

        fillPersonForm(body);
        updateLoaded = true;
        updateFieldAvailability();
        showResponse("GET", endpoint, response.status, body, false);
    } catch (error) {
        updateLoaded = false;
        updateFieldAvailability();
        showResponse("GET", endpoint, "erro", "Nao foi possivel buscar a pessoa para atualizacao.", true);
    }
}

function updateEndpointHelp() {
    endpointHelp.textContent = endpointHelpText[endpointAction.value];
}

function updateFieldAvailability() {
    const action = endpointAction.value;
    const findAllMode = action === "findAll";
    const idOnlyMode = action === "delete" || action === "findById";
    const createMode = action === "create";
    const updateMode = action === "update";
    const updateEditable = updateMode && updateLoaded;

    idInput.disabled = findAllMode;

    firstNameInput.disabled = findAllMode || idOnlyMode || (updateMode && !updateEditable);
    lastNameInput.disabled = findAllMode || idOnlyMode || (updateMode && !updateEditable);
    addressInput.disabled = findAllMode || idOnlyMode || (updateMode && !updateEditable);
    genderInput.disabled = findAllMode || idOnlyMode || (updateMode && !updateEditable);

    if (createMode) {
        idInput.disabled = true;
        idInput.value = "";
        firstNameInput.disabled = false;
        lastNameInput.disabled = false;
        addressInput.disabled = false;
        genderInput.disabled = false;
    }

    if (updateMode) {
        idInput.disabled = false;
    }
}

function handleEndpointChange() {
    updateLoaded = false;
    updateEndpointHelp();
    searchAction.classList.toggle("hidden", endpointAction.value !== "update");
    updateFieldAvailability();
}

endpointAction.addEventListener("change", handleEndpointChange);
searchRecordButton.addEventListener("click", searchRecordForUpdate);
idInput.addEventListener("input", () => {
    if (endpointAction.value !== "update") {
        return;
    }

    updateLoaded = false;
    firstNameInput.value = "";
    lastNameInput.value = "";
    addressInput.value = "";
    genderInput.value = "";
    updateFieldAvailability();
});
sendRequestButton.addEventListener("click", () => callApi(endpointAction.value));

handleEndpointChange();
