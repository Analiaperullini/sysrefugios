const ESTADOS_ANIMAL = {
    RESCATADO: "Rescatado",
    EN_TRANSITO: "En tránsito",
    DERIVADO: "Derivado",
    EN_ADOPCION: "En adopción",
    ADOPTADO: "Adoptado",
};

const ETAPAS_VIDA = {
    CACHORRO: "Cachorro",
    ADULTO: "Adulto",
};

const TAMANIOS = {
    PEQUENO: "Pequeño",
    MEDIANO: "Mediano",
    GRANDE: "Grande",
};

const ESTADOS_RESCATE = {
    EN_CURSO: "En curso",
    FINALIZADO: "Finalizado",
    DERIVADO: "Derivado",
};

const TIPOS_EVENTO = {
    RESCATE: "Rescate",
    TRANSITO: "Tránsito",
    DERIVACION: "Derivación",
    VACUNACION: "Vacunación",
    CASTRACION: "Castración",
    ADOPCION: "Adopción",
    SEGUIMIENTO: "Seguimiento",
    CAMPANIA: "Campaña",
};

const ESTADOS_CAMPANIA = {
    PROGRAMADA: "Programada",
    ACTIVA: "Activa",
    FINALIZADA: "Finalizada",
};

const ESTADOS_ASIGNACION = {
    ASIGNADA: "Asignada",
    REALIZADA: "Realizada",
    CANCELADA: "Cancelada",
};

const ESTADOS_USUARIO = {
    ACTIVO: "Activo",
    BLOQUEADO: "Bloqueado",
    BAJA: "Baja",
};

function etiqueta(tabla, valor) {
    return valor ? tabla[valor] ?? valor : "";
}

function opcionesSelector(tabla, conVacia) {
    const vacia = conVacia ? `<option value="">${conVacia}</option>` : "";
    return vacia + Object.entries(tabla).map(([valor, texto]) => `<option value="${valor}">${texto}</option>`).join("");
}

function formatearFecha(iso) {
    if (!iso) return "";
    const [anio, mes, dia] = iso.split("-");
    return `${dia}/${mes}/${anio}`;
}

function sumarMeses(iso, meses) {
    const [anio, mes, dia] = iso.split("-").map(Number);
    const total = mes - 1 + meses;
    const anioDestino = anio + Math.floor(total / 12);
    const mesDestino = total % 12;
    const ultimoDia = new Date(anioDestino, mesDestino + 1, 0).getDate();
    const diaDestino = Math.min(dia, ultimoDia);
    return `${anioDestino}-${String(mesDestino + 1).padStart(2, "0")}-${String(diaDestino).padStart(2, "0")}`;
}

function hoyIso() {
    const hoy = new Date();
    return `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, "0")}-${String(hoy.getDate()).padStart(2, "0")}`;
}

function enlaceHistorial(animalId, texto) {
    return `<a href="historial.html?animalId=${animalId}">${escaparHtml(texto)}</a>`;
}
