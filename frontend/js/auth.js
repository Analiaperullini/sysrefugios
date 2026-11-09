const CLAVE_SESION = "sysrefugios_sesion";

const MODULOS = [
    { pagina: "inicio.html", texto: "Inicio" },
    { pagina: "animales.html", texto: "Animales" },
    { pagina: "historial.html", texto: "Historial" },
    { pagina: "rescates.html", texto: "Rescates" },
    { pagina: "transitos.html", texto: "Tránsitos" },
    { pagina: "derivaciones.html", texto: "Derivaciones" },
    { pagina: "adoptantes.html", texto: "Adoptantes" },
    { pagina: "adopciones.html", texto: "Adopciones" },
    { pagina: "seguimientos.html", texto: "Seguimientos" },
    { pagina: "vacunaciones.html", texto: "Vacunaciones" },
    { pagina: "castraciones.html", texto: "Castraciones" },
    { pagina: "campanias.html", texto: "Campañas" },
    { pagina: "usuarios.html", texto: "Usuarios" },
    { pagina: "organizaciones.html", texto: "Organizaciones" },
];

function guardarSesion(usuario) {
    sessionStorage.setItem(CLAVE_SESION, JSON.stringify(usuario));
}

function obtenerSesion() {
    const crudo = sessionStorage.getItem(CLAVE_SESION);
    return crudo ? JSON.parse(crudo) : null;
}

async function cerrarSesion() {
    try {
        await api.post("/logout");
    } catch (error) {
    }
    sessionStorage.removeItem(CLAVE_SESION);
    window.location.href = rutaRaiz() + "index.html";
}

function exigirSesion() {
    const sesion = obtenerSesion();
    if (!sesion) {
        window.location.href = rutaRaiz() + "index.html";
        return null;
    }
    if (sesion.cambioObligatorio && !window.location.pathname.endsWith("cambiar-contrasena.html")) {
        window.location.href = "cambiar-contrasena.html";
        return null;
    }
    return sesion;
}

function inicializarPagina(paginaActual) {
    const sesion = exigirSesion();
    if (!sesion) {
        return null;
    }

    const enlaces = MODULOS.map(
        (m) =>
            `<a href="${m.pagina}" class="${m.pagina === paginaActual ? "activo" : ""}">${m.texto}</a>`
    ).join("");

    document.getElementById("encabezado").innerHTML = `
        <div class="marca">Sys<span>Refugios</span></div>
        <nav class="menu-modulos">${enlaces}</nav>
        <div class="menu-usuario">
            <span>${escaparHtml(sesion.nombre)} ${escaparHtml(sesion.apellido)} &middot; ${escaparHtml(sesion.organizacionNombre)}</span>
            <a href="cambiar-contrasena.html">Cambiar contraseña</a>
            <button id="boton-salir" class="boton boton-secundario boton-chico" type="button">Salir</button>
        </div>
    `;
    document.getElementById("boton-salir").addEventListener("click", cerrarSesion);

    return sesion;
}

function agregarVerContrasena() {
    document.querySelectorAll('input[type="password"]').forEach((campo) => {
        const contenedor = document.createElement("div");
        contenedor.className = "campo-contrasena";
        campo.parentNode.insertBefore(contenedor, campo);
        contenedor.appendChild(campo);
        const boton = document.createElement("button");
        boton.type = "button";
        boton.className = "boton-ver-contrasena";
        boton.textContent = "Mostrar";
        boton.setAttribute("aria-label", "Mostrar contraseña");
        boton.addEventListener("click", () => {
            const oculta = campo.type === "password";
            campo.type = oculta ? "text" : "password";
            boton.textContent = oculta ? "Ocultar" : "Mostrar";
            boton.setAttribute("aria-label", oculta ? "Ocultar contraseña" : "Mostrar contraseña");
            campo.focus();
        });
        contenedor.appendChild(boton);
    });
}

agregarVerContrasena();
