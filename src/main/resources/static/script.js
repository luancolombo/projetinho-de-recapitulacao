const idInput = document.getElementById("person-id");
const firstNameInput = document.getElementById("first-name");
const lastNameInput = document.getElementById("last-name");
const addressInput = document.getElementById("address");
const genderInput = document.getElementById("gender");
const endpointAction = document.getElementById("endpoint-action");
const sendRequestButton = document.getElementById("send-request");
const endpointHelp = document.getElementById("endpoint-help");
const responsePanel = document.getElementById("response-panel");
const responseMeta = document.getElementById("response-meta");
const responseBody = document.getElementById("response-body");

const endpointHelpText = {
    findAll: "GET /person lista todas as pessoas mockadas pelo servico.",
    findById: "GET /person/{id} precisa apenas do campo ID.",
    create: "POST /person envia os dados preenchidos no formulario em JSON.",
    update: "PUT /person envia os dados do formulario para atualizar uma pessoa.",
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

function updateEndpointHelp() {
    endpointHelp.textContent = endpointHelpText[endpointAction.value];
}

function updateFieldAvailability() {
    const action = endpointAction.value;
    const findAllMode = action === "findAll";
    const idOnlyMode = action === "delete" || action === "findById";
    const createMode = action === "create";
    const updateMode = action === "update";

    idInput.disabled = findAllMode;

    firstNameInput.disabled = findAllMode || idOnlyMode;
    lastNameInput.disabled = findAllMode || idOnlyMode;
    addressInput.disabled = findAllMode || idOnlyMode;
    genderInput.disabled = findAllMode || idOnlyMode;

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
        firstNameInput.disabled = false;
        lastNameInput.disabled = false;
        addressInput.disabled = false;
        genderInput.disabled = false;
    }
}

function handleEndpointChange() {
    updateEndpointHelp();
    updateFieldAvailability();
}

endpointAction.addEventListener("change", handleEndpointChange);
sendRequestButton.addEventListener("click", () => callApi(endpointAction.value));

handleEndpointChange();
