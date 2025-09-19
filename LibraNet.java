import java.util.*;

// Base abstract class
abstract class LibraryItem {
    protected int id;
    protected String title;
    protected String author;
    protected boolean available = true;
    protected Date borrowDate;

    public LibraryItem(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public void borrow() throws Exception {
        if (!available) throw new Exception("Item already borrowed: " + title);
        available = false;
        borrowDate = new Date();
        System.out.println(title + " borrowed successfully!");
    }

    public double returnItem(int actualDays) {
        if (available) {
            System.out.println(title + " was not borrowed.");
            return 0;
        }
        available = true;
        double fine = actualDays > 14 ? (actualDays - 14) * 10 : 0;
        System.out.println(title + " returned. Fine: Rs." + fine);
        return fine;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getId() {
        return id;
    }

    public abstract void showDetails(Map<Integer, Double> fineMap);
}

// Interface
interface Playable {
    void play();
}

// Book
class Book extends LibraryItem {
    private int pageCount;

    public Book(int id, String title, String author, int pageCount) {
        super(id, title, author);
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public void showDetails(Map<Integer, Double> fineMap) {
        double fine = fineMap.getOrDefault(id, 0.0);
        System.out.println("[Book] ID: " + id + " | Title: " + title + " | Author: " + author +
                           " | Pages: " + pageCount + " | Available: " + available + " | Fine: Rs." + fine);
    }
}

// AudioBook
class AudioBook extends LibraryItem implements Playable {
    private double duration; // minutes

    public AudioBook(int id, String title, String author, double duration) {
        super(id, title, author);
        this.duration = duration;
    }

    @Override
    public void play() {
        System.out.println("Playing audiobook: " + title + " (" + duration + " mins)");
    }

    @Override
    public void showDetails(Map<Integer, Double> fineMap) {
        double fine = fineMap.getOrDefault(id, 0.0);
        System.out.println("[Audiobook] ID: " + id + " | Title: " + title + " | Author: " + author +
                           " | Duration: " + duration + " mins | Available: " + available + " | Fine: Rs." + fine);
    }
}

// E-Magazine
class EMagazine extends LibraryItem {
    private int issueNumber;

    public EMagazine(int id, String title, String author, int issueNumber) {
        super(id, title, author);
        this.issueNumber = issueNumber;
    }

    public void archiveIssue() {
        System.out.println("Archiving issue " + issueNumber + " of " + title);
    }

    @Override
    public void showDetails(Map<Integer, Double> fineMap) {
        double fine = fineMap.getOrDefault(id, 0.0);
        System.out.println("[E-Magazine] ID: " + id + " | Title: " + title + " | Author: " + author +
                           " | Issue No: " + issueNumber + " | Available: " + available + " | Fine: Rs." + fine);
    }
}

// Main
public class LibraNet {
    private static Map<Integer, LibraryItem> library = new HashMap<>();
    private static Map<Integer, Double> fines = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    private static void seedLibrary() {
        library.put(1, new Book(1, "Java Basics", "John Doe", 300));
        library.put(2, new AudioBook(2, "Learn DSA", "Alice Smith", 120));
        library.put(3, new EMagazine(3, "Tech Today", "Editorial Board", 45));
    }

    private static void showBanner() {
        System.out.println("\n======================================");
        System.out.println("      Welcome to LibraNet      ");
        System.out.println(" Your Digital Library Assistant");
        System.out.println("======================================");
    }

    private static void showMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. View all items");
        System.out.println("2. Borrow item");
        System.out.println("3. Return item");
        System.out.println("4. Play audiobook");
        System.out.println("5. Archive magazine issue");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
    }

    public static void main(String[] args) {
        seedLibrary();
        showBanner();

        while (true) {
            showMenu();
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Library Items ---");
                    for (LibraryItem item : library.values()) {
                        item.showDetails(fines);
                    }
                    break;

                case 2:
                    System.out.print("Enter item ID to borrow: ");
                    int borrowId = Integer.parseInt(scanner.nextLine());
                    LibraryItem borrowItem = library.get(borrowId);
                    if (borrowItem != null) {
                        try {
                            borrowItem.borrow();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter item ID to return: ");
                    int returnId = Integer.parseInt(scanner.nextLine());
                    LibraryItem returnItem = library.get(returnId);
                    if (returnItem != null) {
                        System.out.print("Enter actual borrowed days: ");
                        int days = Integer.parseInt(scanner.nextLine());
                        double fine = returnItem.returnItem(days);
                        fines.put(returnId, fines.getOrDefault(returnId, 0.0) + fine);
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter audiobook ID: ");
                    int audioId = Integer.parseInt(scanner.nextLine());
                    LibraryItem audioItem = library.get(audioId);
                    if (audioItem instanceof AudioBook) {
                        ((AudioBook) audioItem).play();
                    } else {
                        System.out.println("Not an audiobook!");
                    }
                    break;

                case 5:
                    System.out.print("Enter magazine ID: ");
                    int magId = Integer.parseInt(scanner.nextLine());
                    LibraryItem magItem = library.get(magId);
                    if (magItem instanceof EMagazine) {
                        ((EMagazine) magItem).archiveIssue();
                    } else {
                        System.out.println("Not a magazine!");
                    }
                    break;

                case 6:
                    System.out.println("Exiting LibraNet. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
