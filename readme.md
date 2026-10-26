Cannamed

O Cannamed e uma aplicacao web desenvolvida como projeto semestral do curso de Analise e Desenvolvimento de Sistemas. A ideia do projeto e facilitar e tornar mais organizado o processo de busca, agendamento e gerenciamento de consultas relacionadas ao uso medicinal da cannabis. A plataforma foi pensada para oferecer uma experiencia simples tanto para pacientes quanto para profissionais e administradores. Os pacientes poderao encontrar profissionais, consultar os horarios disponiveis, realizar o agendamento de consultas e acompanhar seu historico de atendimentos. Os profissionais, por sua vez, poderao organizar sua agenda, visualizar suas consultas e registrar informacoes basicas sobre os atendimentos realizados. O sistema tambem contara com uma area administrativa para auxiliar no gerenciamento da plataforma, permitindo o controle de usuarios, profissionais e consultas.

O principal objetivo do Cannavita e centralizar essas funcoes em uma unica plataforma, tornando o processo de agendamento e acompanhamento das consultas mais pratico e organizado. Dessa forma, o projeto busca atender pacientes que procuram profissionais para consultas, profissionais que precisam gerenciar seus atendimentos e administradores responsaveis pelo funcionamento e controle da plataforma.

 Funcionalidades

 Gestão de Usuários e Autenticação
Cadastro e Autenticação de Usuários: Módulo completo de registro e validação de acesso ao sistema com criptografia/validação de credenciais.
Gerenciamento de Perfis (`Usuario`, `Paciente`, `Medico`, `Admin`): Arquitetura orientada a objetos com controle de permissões por tipo de conta.
Cadastro de Pacientes (`CadastroPacienteService` e `PacienteDAO`): Validação de dados pessoais (Nome, E-mail, CPF, Data de Nascimento) e persistência no banco de dados.

Módulo Médico e de Agendamentos
Busca e Visualização de Profissionais (`MedicoDAO`): Listagem de especialistas cadastrados, especialidades e avaliações.
Visualização de Disponibilidade de Horários: Consulta de dias e horários livres na agenda dos profissionais.
Agendamento de Consultas: Marcação de atendimentos diretamente pelo painel do paciente.
Cancelamento e Gerenciamento de Consultas:Alteração e cancelamento de agendas com atualização em tempo real no banco de dados.
Gerenciamento da Agenda Profissional: Interface para médicos organizarem horários de atendimento e histórico de consultas prestadas.

---

Funcionalidades - Parte 2: Módulos Avançados e Gestão

Acompanhamento e Métricas
Histórico de Consultas: Registro cronológico de consultas realizadas, agendadas e canceladas.
Registro Básico de Acompanhamento: Anotações e evolução do tratamento de pacientes acompanhados na plataforma.
Dashboards Personalizados: Painéis de métricas e indicadores de desempenho e uso do sistema.
Área Administrativa (`AdminDAO`): Painel centralizado de controle da plataforma, exibindo métricas gerais:
   Contagem total de Pacientes cadastrados
   Contagem de Profissionais ativos
   Número de Consultas realizadas/agendadas
   Mapeamento de Usuários ativos no sistema
Controle de Acesso (RBAC): Restrição de menus e recursos de acordo com o nível de privilégio do usuário logado.

---

 Tecnologias Utilizadas

 Backend & Persistência
Java 17:** Linguagem base para desenvolvimento da regra de negócio e modelos de domínio.
Arquitetura DAO (Data Access Object): Padrão de projeto para isolamento da camada de acesso a dados (`AdminDAO`, `MedicoDAO`, `PacienteDAO`, `UsuarioDAO`).
Maven: Gerenciador de dependências e automação do build.
Tomcat 7 (`tomcat7-maven-plugin`): Servidor web de aplicação Java para execução local.
Banco de Dados H2: Banco de dados relacional embutido para ambiente de desenvolvimento (`schema.sql`).

Frontend & Interface
HTML5: Estruturação semântica de todas as páginas do sistema.
CSS3 / Style.css: Estilização personalizada do layout visual da aplicação.


![logo](/img/logo.png)
![TelaPrincipal](/img/TelaPrincipal.png)
![SobreSite](/img/SobreSite.png)
![ComoFunciona](/img/ComoFunciona.png)
![Profissionais](/img/Profissionais.png)
![TelaProfissional](/img/TelaProfissional.png)
![Agendar](/img/Agendar.png)
![Consultas](/img/Consultas.png)
![Entrar](/img/Entrar.png)
![Cadastrar](/img/Cadastrar.png)
![Admin](/img/Admin.png)
