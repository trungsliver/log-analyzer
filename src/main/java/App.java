import service.LogAnalyzerService;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        LogAnalyzerService service = new LogAnalyzerService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== LOG ANALYZER CLI =====");
            System.out.println("1. Phân tích log");
            System.out.println("2. Xem kết quả");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Nhập đường dẫn thư mục log: ");
                    String path = sc.nextLine();
                    service.analyzeLogs(path);
                }
                case 2 -> service.showResults();
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("❌ Lựa chọn không hợp lệ");
            }
        }
    }
}