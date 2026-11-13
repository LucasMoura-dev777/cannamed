<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Recupera o parâmetro "medico" da URL
    String medicoParam = request.getParameter("medico");
    if (medicoParam == null) medicoParam = "thiago";

    // Valores padrão (Dr. Thiago Felipe)
    String nome = "Dr. Thiago Felipe";
    String crm = "123456";
    String local = "São Paulo - Interlagos";
    String iniciais = "TF";
    String especialidade = "Epilepsia";
    String valor = "R$ 100,00";

    if (medicoParam.equals("daniel")) {
        nome = "Dr. Daniel Oliveira";
        local = "São Paulo - Jd. São Luís";
        iniciais = "DO";
        especialidade = "Clínica Geral";
    } else if (medicoParam.equals("matheus")) {
        nome = "Dr. Matheus Medeiros";
        local = "São Paulo - Grajaú";
        iniciais = "MM";
        especialidade = "Dor Crônica";
    } else if (medicoParam.equals("lucas")) {
        nome = "Dr. Lucas Moura";
        local = "São Paulo - Interlagos";
        iniciais = "LM";
        especialidade = "Neurologia";
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CANNAMED - Agendar Consulta com <%= nome %></title>
    <link rel="stylesheet" href="style-agendar.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<header class="navbar">
    <a href="index.jsp" class="logo">CANNAMED</a>
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
    <!-- Formulário que envia a data, horário e o médico escolhido para o Servlet -->
    <form action="agendar" method="POST">
        <!-- Campo oculto para enviar o ID/nome do médico selecionado -->
        <input type="hidden" name="medicoEscolhido" value="<%= medicoParam %>">

        <div class="agendar-layout">

            <!-- Resumo do Médico Dinâmico -->
            <div class="card-medico-resumo">
                <div class="medico-header">
                    <div class="avatar-circle"><%= iniciais %></div>
                    <h3 class="medico-nome"><%= nome %></h3>
                </div>
                <div class="medico-detalhes">
                    <p>CRM : <%= crm %></p>
                    <p><%= local %></p>
                    <p>Avaliação : 5.0</p>
                    <p>Especialidade: <%= especialidade %></p>
                </div>
                <div class="medico-preco">
                    <span><%= valor %></span>
                </div>
            </div>

            <!-- Escolha a Data -->
            <div class="card-datas">
                <h3>Escolha a data</h3>
                <div class="datas-grid">
                    <button type="button" class="btn-opcao active" data-value="17/08">17/08</button>
                    <button type="button" class="btn-opcao" data-value="18/08">18/08</button>
                    <button type="button" class="btn-opcao" data-value="19/08">19/08</button>
                    <button type="button" class="btn-opcao" data-value="20/08">20/08</button>
                    <button type="button" class="btn-opcao" data-value="21/08">21/08</button>
                    <button type="button" class="btn-opcao" data-value="22/08">22/08</button>
                    <button type="button" class="btn-opcao" data-value="23/08">23/08</button>
                    <button type="button" class="btn-opcao" data-value="24/08">24/08</button>
                    <button type="button" class="btn-opcao" data-value="25/08">25/08</button>
                </div>
                <input type="hidden" name="dataEscolhida" id="dataEscolhida" value="17/08">
            </div>

            <!-- Escolha o Horário -->
            <div class="card-horarios">
                <h3>Escolha o horário</h3>
                <div class="horarios-list">
                    <button type="button" class="btn-opcao active" data-value="10:00">10:00</button>
                    <button type="button" class="btn-opcao" data-value="13:00">13:00</button>
                    <button type="button" class="btn-opcao" data-value="14:00">14:00</button>
                </div>
                <input type="hidden" name="horarioEscolhido" id="horarioEscolhido" value="10:00">
            </div>

        </div>

        <div class="confirmar-container">
            <button type="submit" class="btn-confirmar">Confirmar Consulta</button>
        </div>
    </form>
</main>

<script>
    function configurarSelecao(containerSelector, inputId) {
        const botoes = document.querySelectorAll(containerSelector + ' .btn-opcao');
        const inputHidden = document.getElementById(inputId);

        botoes.forEach(botao => {
            botao.addEventListener('click', () => {
                botoes.forEach(b => b.classList.remove('active'));
                botao.classList.add('active');
                inputHidden.value = botao.getAttribute('data-value');
            });
        });
    }

    configurarSelecao('.card-datas', 'dataEscolhida');
    configurarSelecao('.card-horarios', 'horarioEscolhido');
</script>

</body>
</html>