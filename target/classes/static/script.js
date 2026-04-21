const form = document.querySelector("#calculator-form");
const operation = document.querySelector("#operation");
const numberOne = document.querySelector("#number-one");
const numberTwo = document.querySelector("#number-two");
const numberTwoLabel = document.querySelector("#number-two-label");
const resultPanel = document.querySelector(".result-panel");
const resultValue = document.querySelector("#result-value");
const endpointValue = document.querySelector("#endpoint-value");

const operations = {
    sum: "/math/sum/{numberOne}/{numberTwo}",
    subtraction: "/math/subtraction/{numberOne}/{numberTwo}",
    multiplication: "/math/multiplication/{numberOne}/{numberTwo}",
    division: "/math/division/{numberOne}/{numberTwo}",
    mean: "/math/mean/{numberOne}/{numberTwo}",
    sqrt: "/math/sqrt/{numberOne}"
};

function isSquareRoot() {
    return operation.value === "sqrt";
}

function updateSecondFieldVisibility() {
    const shouldHide = isSquareRoot();
    numberTwo.classList.toggle("hidden", shouldHide);
    numberTwoLabel.classList.toggle("hidden", shouldHide);
    numberTwo.disabled = shouldHide;
}

function buildEndpoint() {
    const firstValue = encodeURIComponent(numberOne.value.trim());
    const secondValue = encodeURIComponent(numberTwo.value.trim());
    const template = operations[operation.value];

    return template
        .replace("{numberOne}", firstValue)
        .replace("{numberTwo}", secondValue);
}

function showResult(message, endpoint, isError) {
    resultPanel.classList.toggle("error", isError);
    resultValue.textContent = message;
    endpointValue.textContent = endpoint;
}

async function calculate(event) {
    event.preventDefault();

    const endpoint = buildEndpoint();

    try {
        const response = await fetch(endpoint);
        const data = await response.json();

        if (!response.ok) {
            showResult(data.message || "Nao foi possivel calcular.", endpoint, true);
            return;
        }

        showResult(data, endpoint, false);
    } catch (error) {
        showResult("Nao foi possivel chamar o endpoint.", endpoint, true);
    }
}

operation.addEventListener("change", updateSecondFieldVisibility);
form.addEventListener("submit", calculate);

updateSecondFieldVisibility();
