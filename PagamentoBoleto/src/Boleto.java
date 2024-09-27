import java.util.Scanner;

public class Boleto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao = 0;
        String[] boxes = new String[4];

        do {
            exibirmenu();
            System.out.print("Digite a opção: ");
            if (sc.hasNextInt()) {
                opcao = sc.nextInt();
                sc.nextLine();  

                if (opcao == 1) {
                    boolean validosBoxes = true;
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Digite o BOXE-" + (i + 1) + ": ");
                        boxes[i] = sc.nextLine().trim();
  
                        if (!validarTamanho(boxes[i])) {
                            System.out.println("Os BOXES devem conter 12 dígitos.");
                            System.out.println("Pressione ENTER para continuar...");
                            sc.nextLine();
                            validosBoxes = false;
                            break;
                        }
                        if (!validarDacBoxe(boxes[i])) {
                            System.out.println("BOXE-" + (i + 1) + " inválido.");
                            System.out.println("Pressione ENTER para continuar...");
                            sc.nextLine();
                            validosBoxes = false;
                            break;
                        }
                    }

                    if (validosBoxes) {
                        System.out.println("Pagamento feito com sucesso!");
                        System.out.println("Pressione ENTER para continuar...");
                        sc.nextLine();
                    }

                } else if (opcao == 2) {
                    try {
                        System.out.println("Imprimindo a 2ª via do boleto...");

                        System.out.print("Digite o CÓDIGO (836): ");
                        String codigo = sc.nextLine();

                        System.out.print("Digite o VALOR DA FATURA(165,27): ");
                        String valorFatura = sc.nextLine();
                        valorFatura = String.format("%011d", Integer.parseInt(valorFatura.replace(",", "")));

                        System.out.print("Digite a IDENTIFICAÇÃO EMPRESA(0148000): ");
                        String identificacao = sc.nextLine();
                        identificacao = String.format("%07d", Integer.parseInt(identificacao));

                        System.out.print("Digite a UNIDADE CONSUMIDORA(1020749): ");
                        String unidade = sc.nextLine();
                        unidade = String.format("%07d", Integer.parseInt(unidade));

                        System.out.print("Digite o ANO-MES (AAAAMM - 201907): ");
                        String anoMes = sc.nextLine();
                        anoMes = String.format("%06d", Integer.parseInt(anoMes));

                        System.out.print("Digite o SEQUENCIAL(101): ");
                        String sequencial = sc.nextLine();
                        sequencial = String.format("%07d", Integer.parseInt(sequencial));

                        String boleto = codigo + valorFatura + identificacao + unidade + anoMes + "9" + sequencial + "9";
                        String dacGeral = calcularDac(boleto);
                        boleto = codigo + dacGeral + valorFatura + identificacao + unidade + anoMes + "9" + sequencial + "9";
                        System.out.println("Segunda VIA DO BOLETO: " + boleto);
                        System.out.println("Aperte ENTER para continuar...");
                        sc.nextLine();  

                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida, por favor insira valores numéricos corretamente.");
                        System.out.println("Aperte ENTER para continuar...");
                        sc.nextLine();  
                    }
                } else if (opcao == 3) {
                    System.out.println("Programa finalizado...");
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                    System.out.println("Aperte ENTER para continuar...");
                    sc.nextLine();  
                }
            } else {
                System.out.println("Entrada inválida, por favor insira um número.");
                System.out.println("Aperte ENTER para continuar...");
                sc.nextLine();  
                sc.nextLine();  
            }
        } while (opcao != 3);

        sc.close();
    }

    public static void exibirmenu() {
        System.out.println("------------------------------------------");
        System.out.println("             CONTAS E TRIBUTOS");
        System.out.println("     (Água, Luz, Telefone, IPTU, ISS)");
        System.out.println("------------------------------------------");
        System.out.println(" 1) Pagamento c/Código de Barras");
        System.out.println(" 2) Imprimir 2ª Via de Boleto");
        System.out.println(" 3) Sair");
        System.out.println("------------------------------------------");
    }

    public static boolean validarTamanho(String numero) {
        return numero.length() == 12;
    }

    public static boolean validarDacBoxe(String numero) {
        int[] multiplicadores = {2, 1}; 
        int soma = 0, digito, produto, resto, dacCalculado, dacVerificador;

        for (int i = numero.length() - 2, j = 0; i >= 0; i--, j++) {
            digito = Character.getNumericValue(numero.charAt(i));
            produto = digito * multiplicadores[j % 2];
            soma += somarDigitos(produto);
        }

        dacVerificador = Character.getNumericValue(numero.charAt(11));
        resto = soma % 10;
        dacCalculado = (resto == 0) ? 0 : 10 - resto;
        return dacCalculado == dacVerificador;
    }

    public static int somarDigitos(int numero) {
        int soma = 0;
        while (numero > 0) {
            soma += numero % 10;
            numero /= 10;
        }
        return soma;
    }

    public static String calcularDac(String numero) {
        int[] multiplicadores = {2, 1};
        int soma = 0;

        for (int i = numero.length() - 1, j = 0; i >= 0; i--, j++) {
            int digito = Character.getNumericValue(numero.charAt(i));
            int produto = digito * multiplicadores[j % 2];
            soma += somarDigitos(produto);
        }
        int resto = soma % 10;
        int dacCalculado = (resto == 0) ? 0 : 10 - resto;

        return String.valueOf(dacCalculado);
    }
}