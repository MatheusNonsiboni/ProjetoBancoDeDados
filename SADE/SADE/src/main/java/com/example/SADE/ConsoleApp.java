package com.example.SADE;

import com.example.SADE.Controller.*;
import com.example.SADE.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
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
        System.out.println("Sistema de Avaliação de Desempenho Escolar (SADE)");

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Cadastrar Região");
            System.out.println("2 - Cadastrar Escola");
            System.out.println("3 - Cadastrar Disciplina (com desempenho)");
            System.out.println("4 - Listar Desempenho por Escola");
            System.out.println("5 - Ranking Geral por IDEB");
            System.out.println("6 - Ranking por Disciplina e Região");
            System.out.println("7 - Cadastrar Indicador Educacional");
            System.out.println("8 - Exibir Todos os Dados");
            System.out.println("0 - Sair");

            int opcao = readInt("Opção: ", 0, 8);

            switch (opcao) {
                case 1 -> cadastrarRegiao();
                case 2 -> cadastrarEscola();
                case 3 -> cadastrarDisciplinaComDesempenho();
                case 4 -> listarDesempenhoPorEscola();
                case 5 -> rankingPorIDEB();
                case 6 -> rankingPorDisciplinaRegiao();
                case 7 -> cadastrarIndicador();
                case 8 -> exibirDados();
                case 0 -> {
                    System.out.println("Encerrando o sistema...");
                    return;
                }
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
            System.out.println("Nenhuma região cadastrada! Cadastre uma primeiro.");
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
    }

    private void cadastrarDisciplinaComDesempenho() {
        List<Escola> escolas = escolaController.listar();
        if (escolas.isEmpty()) {
            System.out.println("É preciso ter escolas cadastradas antes!");
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

        Disciplina disciplinaSalva = disciplinaController.cadastrar(disciplina);

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
    }

    private void cadastrarIndicador() {
        List<Escola> escolas = escolaController.listar();
        if (escolas.isEmpty()) {
            System.out.println("É preciso ter escolas cadastradas antes!");
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
    }

    private void listarDesempenhoPorEscola() {
        List<Escola> escolas = escolaController.listar();
        if (escolas.isEmpty()) {
            System.out.println("Não há escolas cadastradas.");
            return;
        }

        System.out.println("Selecione a escola:");
        for (int i = 0; i < escolas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, escolas.get(i).getNome());
        }

        int escolha = readInt("Escola: ", 1, escolas.size());
        Escola escola = escolas.get(escolha - 1);

        List<DesempenhoDisciplina> desempenhos = desempenhoController.listar();
        System.out.println("\nDesempenhos da escola " + escola.getNome() + ":");

        desempenhos.stream()
                .filter(d -> d.getEscola().getId_escola().equals(escola.getId_escola()))
                .forEach(d -> System.out.printf(
                        "Disciplina: %s | Ano: %d | Média: %.2f | Frequência: %.2f%%%n",
                        d.getDisciplina().getNome(),
                        d.getAno_letivo(),
                        d.getMedia_disciplina(),
                        d.getFrequencia_media()
                ));
    }

    private void rankingPorIDEB() {
        List<Escola> escolas = escolaController.listar();
        List<IndicadorEducacional> indicadores = indicadorController.listar();

        System.out.println("\nRanking Geral por IDEB:");

        indicadores.stream()
                .sorted(Comparator.comparingDouble(IndicadorEducacional::getIdeb).reversed())
                .forEach(ind -> System.out.printf(
                        "%s — IDEB: %.2f (%d)\n",
                        ind.getEscola().getNome(),
                        ind.getIdeb(),
                        ind.getAno_letivo()
                ));
    }

    private void rankingPorDisciplinaRegiao() {
    List<Regiao> regioes = regiaoController.listar();
    if (regioes.isEmpty()) {
        System.out.println("Nenhuma região cadastrada.");
        return;
    }

    List<Disciplina> disciplinas = disciplinaController.listar();
    if (disciplinas.isEmpty()) {
        System.out.println("Nenhuma disciplina cadastrada.");
        return;
    }

    System.out.println("Selecione a região:");
    for (int i = 0; i < regioes.size(); i++) {
        System.out.printf("%d - %s%n", i + 1, regioes.get(i).getNome());
    }
    int escReg = readInt("Região: ", 1, regioes.size());
    Regiao reg = regioes.get(escReg - 1);

    System.out.println("Selecione a disciplina:");
    for (int i = 0; i < disciplinas.size(); i++) {
        System.out.printf("%d - %s%n", i + 1, disciplinas.get(i).getNome());
    }
    int escDisc = readInt("Disciplina: ", 1, disciplinas.size());
    Disciplina disc = disciplinas.get(escDisc - 1);

    List<DesempenhoDisciplina> desempenhos = desempenhoController.listar();

    System.out.println("\n");
    System.out.println("Ranking da Região: " + reg.getNome());
    System.out.println("Disciplina: " + disc.getNome());
    System.out.println("\n");

    List<DesempenhoDisciplina> filtrados = desempenhos.stream()
            .filter(d ->
                    d.getDisciplina().getId_disciplina().equals(disc.getId_disciplina()) &&
                    d.getEscola().getRegiao().getId_regiao().equals(reg.getId_regiao())
            )
            .sorted(Comparator.comparingDouble(DesempenhoDisciplina::getMedia_disciplina).reversed())
            .toList();

    if (filtrados.isEmpty()) {
        System.out.println("Nenhuma escola dessa região possui desempenho cadastrado nessa disciplina.");
        return;
    }

    int pos = 1;
    for (DesempenhoDisciplina d : filtrados) {
        System.out.printf("%dº - %s — Média: %.2f (%d)%n",
                pos++,
                d.getEscola().getNome(),
                d.getMedia_disciplina(),
                d.getAno_letivo()
        );
    }
}

    private void exibirDados() {
        List<Regiao> regioes = regiaoController.listar();
        List<Escola> escolas = escolaController.listar();
        List<Disciplina> disciplinas = disciplinaController.listar();
        List<DesempenhoDisciplina> desempenhos = desempenhoController.listar();
        List<IndicadorEducacional> indicadores = indicadorController.listar();

        for (Regiao r : regioes) {
            System.out.println("\nRegião: " + r.getNome() + " (Mesorregião: " + r.getMesorregiao() + ")");
            for (Escola e : escolas) {
                if (e.getRegiao().getId_regiao().equals(r.getId_regiao())) {
                    System.out.println("  Escola: " + e.getNome() +
                            " (" + e.getTipo_localizacao() + " - " + e.getCidade() + ")");

                    indicadores.stream()
                            .filter(i -> i.getEscola().getId_escola().equals(e.getId_escola()))
                            .forEach(i -> System.out.printf("    IDEB: %.2f | Evasão: %.2f%%%n",
                                    i.getIdeb(), i.getTaxa_evasao()));

                    desempenhos.stream()
                            .filter(d -> d.getEscola().getId_escola().equals(e.getId_escola()))
                            .forEach(d -> System.out.printf("    %s - Média: %.2f | Frequência: %.2f%%%n",
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
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Valor fora do intervalo [%d - %d]%n", min, max);
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Valor fora do intervalo [%.2f - %.2f]%n", min, max);
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
            }
        }
    }
}