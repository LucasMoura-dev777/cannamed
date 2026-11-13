<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String medicoParam = request.getParameter("medico");
    if (medicoParam == null) medicoParam = "thiago";

    String nome = "Dr. Thiago Felipe";
    String crm = "CRM: 123456";
    String local = "São Paulo - Interlagos";
    String iniciais = "TF";
    String especialidade = "Epilepsia";
    String sobre = "Especialista em epilepsia, dor crônica e tratamentos com canabinoides há mais de 10 anos.";
    String valor = "R$ 100,00";

    if (medicoParam.equals("daniel")) {
        nome = "Dr. Daniel Oliveira";
        local = "São Paulo - Jd. São Luís";
        iniciais = "DO";
        especialidade = "Clínica Geral";
        sobre = "Especialista em medicina integrativa e acompanhamento clínico com cannabis medicinal.";
    } else if (medicoParam.equals("matheus")) {
        nome = "Dr. Matheus Medeiros";
        local = "São Paulo - Grajaú";
        iniciais = "MM";
        especialidade = "Dor Crônica";
        sobre = "Focado no tratamento humanizado de pacientes com fibromialgia e dores refratárias.";
    } else if (medicoParam.equals("lucas")) {
        nome = "Dr. Lucas Moura";
        local = "São Paulo - Interlagos";
        iniciais = "LM";
        especialidade = "Neurologia";
        sobre = "Neurologista dedicado a pesquisas e terapias avançadas com canabinoides medicinais.";
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CANNAMED - <%= nome %></title>
    <link rel="stylesheet" href="style-perfil.css">
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
        <a href="minhas-consultas.jsp">Consultas</a>
        <a href="#">Sobre</a>
        <a href="index.jsp#profissionais">Profissionais</a>
    </nav>
    <div class="auth-buttons">
        <a href="entrar.jsp" class="btn-login">Entrar</a>
        <a href="cadastro.jsp" class="btn-register-nav">Criar conta</a>
    </div>
</header>

<main class="main-container">
    <div class="perfil-layout">

        <div class="card-medico-detalhe">
            <div class="medico-header">
                <div class="avatar-circle"><%= iniciais %></div>
                <div class="medico-info-principal">
                    <h1><%= nome %></h1>
                    <p class="crm"><%= crm %> | <%= especialidade %></p>
                    <p class="localizacao"><%= local %></p>
                </div>
            </div>

            <div class="medico-sobre">
                <h3>Sobre</h3>
                <p><%= sobre %></p>
            </div>

            <div class="detalhes-grid">
                <div class="detalhe-box">
                    <span class="detalhe-label">Atendimento</span>
                    <span class="detalhe-valor">Online</span>
                </div>
                <div class="detalhe-box">
                    <span class="detalhe-label">Valor da consulta</span>
                    <span class="detalhe-valor"><%= valor %></span>
                </div>
            </div>
        </div>

        <div class="card-agendar">
            <h2>Agendar consulta</h2>
            <p class="agendar-desc">Escolha data e horário e confirme em poucos passos.</p>
            <a href="agendar.jsp?medico=<%= medicoParam %>" class="btn-agendar">Agendar</a>
        </div>

    </div>
</main>

</body>
</html>