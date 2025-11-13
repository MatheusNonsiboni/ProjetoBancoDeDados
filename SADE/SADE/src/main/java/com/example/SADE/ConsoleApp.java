package com.example.SADE;

import com.example.SADE.Controller.*;
import com.example.SADE.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    @Autowired
    private RegiaoController regiaoController;
    @Autowired
    private EscolaController escolaController;
    @Autowired
    private DisciplinaController disciplinaController;
    @Autowired
    private DesempenhoDisciplinaController desempenhoController;
    @Autowired
    private IndicadorEducacionalController indicadorController;

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void run(String... args) {
        System.out.println("===== Sistema de Avaliação de Desempenho Escolar (SADE) =====");

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Cadastrar Região");
            System.out.println("2 - Cadastrar Escola");
            System.out.println("3 - Cadastrar Disciplina (com vínculo e desempenho)");
            System.out.println("4 - Cadastrar Desempenho por Disciplina (separado)");
            System.out.println("5 - Cadastrar Indicador Educacional");
            System.out.println("6 - Exibir Dados do Sistema");
            System.out.println("0 - Sair");

            int opcao = readInt("Opção: ", 0, 6);

            switch (opcao) {
                case 1 -> cadastrarRegiao();
                case 2 -> cadastrarEscola();
                case 3 -> cadastrarDisciplinaComDesempenho(); // nova lógica
                case 4 -> cadastrarDesempenho(); // modo separado ainda disponível
                case 5 -> cadastrarIndicador();
                case 6 -> exibirDados();
                case 0 -> {
                    System.out.println("Encerrando o sistema...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarRegiao() {
        String nome = readNonEmptyLine("Nome da Região: ");
        String meso = readNonEmptyLine("Mesorregião: ");

        Regiao r = new Regiao();
        r.setNome(nome);
        r.setMesorregiao(meso);
        regiaoController.cadastrar(r);
        System.out.println("✅ Região cadastrada!");
    }

    private void cadastrarEscola() {
        String nome = readNonEmptyLine("Nome da Escola: ");
        String codigo = readNonEmptyLine("Código MEC: ");
        String cidade = readNonEmptyLine("Cidade: ");
        String tipo;
        while (true) {
            tipo = readNonEmptyLine("Tipo (Urbana/Rural): ");
            if (tipo.equalsIgnoreCase("Urbana") || tipo.equalsIgnoreCase("Rural")) break;
            System.out.println("Digite 'Urbana' ou 'Rural'.");
        }

        List<Regiao> regioes = regiaoController.listar();
        if (regioes.isEmpty()) {
            System.out.println("⚠️ Nenhuma região cadastrada! Cadastre uma primeiro.");
            return;
        }

        System.out.println("Selecione a região:");
        for (int i = 0; i < regioes.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, regioes.get(i).getNome());
        }
        int escolha = readInt("Região (número): ", 1, regioes.size());
        Regiao regiao = regioes.get(escolha - 1);

        Escola e = new Escola();
        e.setNome(nome);
        e.setCodigo_mec(codigo);
        e.setCidade(cidade);
        e.setTipo_localizacao(tipo);
        e.setRegiao(regiao);

        escolaController.cadastrar(e);
        System.out.println("✅ Escola cadastrada!");
    }

    /**
     * NOVA: cadastra a disciplina pedindo antes a escola,
     * e já cria um DesempenhoDisciplina para vincular a disciplina criada à escola.
     */
    private void cadastrarDisciplinaComDesempenho() {
        List<Escola> escolas = escolaController.listar();
        if (escolas.isEmpty()) {
            System.out.println("⚠️ É preciso ter escolas cadastradas antes!");
            return;
        }

        System.out.println("Selecione a escola para associar a disciplina:");
        for (int i = 0; i < escolas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, escolas.get(i).getNome());
        }
        int idxEscola = readInt("Escola (número): ", 1, escolas.size());
        Escola escola = escolas.get(idxEscola - 1);

        String nomeDisc = readNonEmptyLine("Nome da Disciplina: ");
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(nomeDisc);

        // salva disciplina e recupera o objeto salvo (controller retorna o salvo)
        Disciplina disciplinaSalva = disciplinaController.cadastrar(disciplina);
        System.out.println("✅ Disciplina cadastrada com ID temporário: " + disciplinaSalva.getId_disciplina());

        // Agora cadastra o desempenho vinculado à escola selecionada e à disciplina criada
        int ano = readInt("Ano letivo: ", 1900, 3000);
        double media = readDouble("Média da disciplina (0-10): ", 0.0, 10.0);
        double freq = readDouble("Frequência média (%) (0-100): ", 0.0, 100.0);

        DesempenhoDisciplina d = new DesempenhoDisciplina();
        d.setEscola(escola);
        d.setDisciplina(disciplinaSalva);
        d.setAno_letivo(ano);
        d.setMedia_disciplina(media);
        d.setFrequencia_media(freq);

        desempenhoController.cadastrar(d);
        System.out.println("✅ Desempenho cadastrado e vinculado à disciplina criada!");
    }

    private void cadastrarDesempenho() {
        List<Escola> escolas = escolaController.listar();
        List<Disciplina> disciplinas = disciplinaController.listar();
        if (escolas.isEmpty() || disciplinas.isEmpty()) {
            System.out.println("⚠️ É preciso ter escolas e disciplinas cadastradas antes!");
            return;
        }

        System.out.println("Selecione a escola:");
        for (int i = 0; i < escolas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, escolas.get(i).getNome());
        }
        int escEscola = readInt("Escola (número): ", 1, escolas.size());
        Escola escola = escolas.get(escEscola - 1);

        System.out.println("Selecione a disciplina:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, disciplinas.get(i).getNome());
        }
        int escDisc = readInt("Disciplina (número): ", 1, disciplinas.size());
        Disciplina disciplina = disciplinas.get(escDisc - 1);

        int ano = readInt("Ano letivo: ", 1900, 3000);
        double media = readDouble("Média da disciplina (0-10): ", 0.0, 10.0);
        double freq = readDouble("Frequência média (%) (0-100): ", 0.0, 100.0);

        DesempenhoDisciplina d = new DesempenhoDisciplina();
        d.setEscola(escola);
        d.setDisciplina(disciplina);
        d.setAno_letivo(ano);
        d.setMedia_disciplina(media);
        d.setFrequencia_media(freq);

        desempenhoController.cadastrar(d);
        System.out.println("✅ Desempenho cadastrado!");
    }

    private void cadastrarIndicador() {
        List<Escola> escolas = escolaController.listar();
        if (escolas.isEmpty()) {
            System.out.println("⚠️ É preciso ter escolas cadastradas antes!");
            return;
        }

        System.out.println("Selecione a escola:");
        for (int i = 0; i < escolas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, escolas.get(i).getNome());
        }
        int esc = readInt("Escola (número): ", 1, escolas.size());
        Escola escola = escolas.get(esc - 1);

        int ano = readInt("Ano letivo: ", 1900, 3000);
        double ideb = readDouble("IDEB (0-10): ", 0.0, 10.0);
        double evasao = readDouble("Taxa de evasão (%) (0-100): ", 0.0, 100.0);

        IndicadorEducacional ind = new IndicadorEducacional();
        ind.setEscola(escola);
        ind.setAno_letivo(ano);
        ind.setIdeb(ideb);
        ind.setTaxa_evasao(evasao);

        indicadorController.cadastrar(ind);
        System.out.println("✅ Indicador cadastrado!");
    }

    private void exibirDados() {
        List<Regiao> regioes = regiaoController.listar();
        List<Escola> escolas = escolaController.listar();
        List<Disciplina> disciplinas = disciplinaController.listar();
        List<DesempenhoDisciplina> desempenhos = desempenhoController.listar();
        List<IndicadorEducacional> indicadores = indicadorController.listar();

        for (Regiao r : regioes) {
            System.out.println("\n🌎 Região: " + r.getNome() + " (Mesorregião: " + r.getMesorregiao() + ")");
            for (Escola e : escolas) {
                if (e.getRegiao() != null && e.getRegiao().getId_regiao().equals(r.getId_regiao())) {
                    System.out.println("  🏫 Escola: " + e.getNome() +
                            " (" + e.getTipo_localizacao() + " - " + e.getCidade() + ")");
                    System.out.println("    Indicadores:");
                    indicadores.stream()
                            .filter(i -> i.getEscola().getId_escola().equals(e.getId_escola()))
                            .forEach(i -> System.out.printf("      IDEB: %.2f | Evasão: %.2f%%%n",
                                    i.getIdeb(), i.getTaxa_evasao()));

                    System.out.println("    Disciplinas e Desempenho:");
                    desempenhos.stream()
                            .filter(d -> d.getEscola().getId_escola().equals(e.getId_escola()))
                            .forEach(d -> System.out.printf("      %s - Média: %.2f | Frequência: %.2f%%%n",
                                    d.getDisciplina().getNome(),
                                    d.getMedia_disciplina(),
                                    d.getFrequencia_media()));
                }
            }
        }
    }

    private String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            if (line != null && !line.trim().isEmpty()) return line.trim();
            System.out.println("Entrada vazia. Tente novamente.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val < min || val > max) {
                    System.out.printf("Valor fora do intervalo [%d - %d]. Tente novamente.%n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido. Digite um inteiro.");
            }
        }
    }

    private double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                double val = Double.parseDouble(line.trim());
                if (val < min || val > max) {
                    System.out.printf("Valor fora do intervalo [%.2f - %.2f]. Tente novamente.%n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido. Digite um valor numérico (ex: 7.5).");
            }
        }
    }
}