<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CANNAMED - Minhas Consultas</title>
    <link rel="stylesheet" href="style-consultas.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<header class="navbar">
    <div class="logo">
        CANNAMED
    </div>
    <nav class="nav-links">
        <a href="index.jsp">Início</a>
        <a href="minhas-consultas.jsp" class="active">Consultas</a>
        <a href="#">Sobre</a>
        <a href="#">Profissionais</a>
    </nav>
    <div class="auth-buttons">
        <a href="entrar.jsp" class="btn-login">Entrar</a>
        <a href="cadastro.jsp" class="btn-register-nav">Criar conta</a>
    </div>
</header>

<main class="main-container">
    <div class="consultas-wrapper">
        <h1 class="page-title">Minhas Consultas</h1>

        <!-- Cartão da Consulta com o formulário de cancelamento -->
        <div class="card-consulta">
            <div class="avatar-circle">TF</div>

            <div class="consulta-info">
                <span class="info-item">Data: 17/08</span>
                <span class="info-item">Horário: 10:00</span>
            </div>

            <form action="cancelar-consulta" method="POST" style="margin: 0;">
                <!-- ID da consulta para sabermos qual cancelar no backend -->
                <input type="hidden" name="idConsulta" value="1">
                <button type="submit" class="btn-cancelar" onclick="return confirm('Tem certeza que deseja cancelar esta consulta?')">Cancelar</button>
            </form>
        </div>
    </div>
</main>

</body>
</html>