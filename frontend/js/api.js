const BASE_API = `http://${window.location.hostname}:8080/api`;

function rutaRaiz() {
    return window.location.pathname.includes("/pages/") ? "../" : "";
}

async function pedidoApi(ruta, opciones = {}) {
    let respuesta;
    try {
        respuesta = await fetch(BASE_API + ruta, {
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            ...opciones,
        });
    } catch (error) {
        throw new Error(
            "No se pudo conectar con el servidor. Verificá que el backend esté corriendo en el puerto 8080."
        );
    }

    if (respuesta.status === 204) {
        return null;
    }

    const cuerpo = await respuesta.json().catch(() => null);

    if (respuesta.status === 401 && ruta !== "/login") {
        sessionStorage.removeItem("sysrefugios_sesion");
        window.location.href = rutaRaiz() + "index.html?expirada=1";
        return new Promise(() => {});
    }

    if (respuesta.status === 403 && cuerpo?.codigo === "CAMBIO_OBLIGATORIO") {
        window.location.href = rutaRaiz() + "pages/cambiar-contrasena.html";
        return new Promise(() => {});
    }

    if (!respuesta.ok) {
        const mensaje = cuerpo?.mensaje || `Error ${respuesta.status}`;
        const detalle = cuerpo?.errores
            ? Object.entries(cuerpo.errores).map(([campo, detalle]) => `${campo}: ${detalle}`).join(", ")
            : "";
        throw new Error(detalle ? `${mensaje}: ${detalle}` : mensaje);
    }

    return cuerpo;
}

const api = {
    get: (ruta) => pedidoApi(ruta),
    post: (ruta, cuerpo) => pedidoApi(ruta, { method: "POST", body: JSON.stringify(cuerpo) }),
    put: (ruta, cuerpo) => pedidoApi(ruta, { method: "PUT", body: JSON.stringify(cuerpo) }),
    del: (ruta) => pedidoApi(ruta, { method: "DELETE" }),
};

function escaparHtml(texto) {
    if (texto === null || texto === undefined) return "";
    return String(texto)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}
