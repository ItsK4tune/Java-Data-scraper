import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LinkFind {
    public static void main(String[] args) {
        String homepageURL = "https://vnexpress.net"; // Website mục tiêu (Sau sẽ đổi sang input dạng .txt)
        String outputFile = "Data/Url.txt"; // Tên file xuất ra

        try {
            Set<String> blockchainURLs = findURLs(homepageURL);

            saveURLsToFile(blockchainURLs, outputFile);

            System.out.println("URLs found saved to: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> findURLs(String homepageURL) throws IOException {
        Set<String> URLs = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Đẩy vào homepageURL
        stack.push(homepageURL);

        // Tìm kiếm Url bằng DFS
        while (!stack.isEmpty() && URLs.size() <= 10) {
            // Lấy Url đầu trong stack làm Url mục tiêu
            String currentURL = stack.pop();

            // Kiểm tra Url mục tiêu đã xét chưa
            if (!visited.contains(currentURL)) {
                visited.add(currentURL);

                // Giới hạn route của Url (<<<<<<<<<<<< Mày sửa chỗ này >>>>>>>>>>>>>>)
                if (currentURL.equals("https://vnexpress.net") || (currentURL.startsWith("https://vnexpress.net/") && currentURL.length() >= 35)) { 
                    URLs.add(currentURL); // Add the URL to the set of blockchain URLs
                    
                    // Tạo kết nối HTTP đến Url mục tiêu
                    Document doc = Jsoup.connect(currentURL).get();

                    // Lấy các phần tử chứa tag <a href="">
                    Elements links = doc.select("a[href]");
    
                    for (Element link : links) {
                        // Kiểm tra điều kiện của Url (<<<<<<<<<<<< Mày thêm bộ lọc chỗ này >>>>>>>>>>>>>>)


                        // Lấy Root Url của phần tử
                        String absUrl = link.absUrl("href");

                        // Đẩy Root Url vào stack
                        stack.push(absUrl);
                    }
                }
            }
        }

        return URLs;
    }

    public static void saveURLsToFile(Set<String> Urls, String fileName) throws IOException {
        // Tạo kết nối viết với file Url.txt 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String Url : Urls) {
                writer.write(Url);
                writer.newLine(); // Xuống dòng
            }
            writer.close();
        }

    }
}
