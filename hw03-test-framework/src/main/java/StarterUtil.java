public class StarterUtil {
    public static void main(String[] args){
        if (args.length < 1)
            return;
        var tester = new Tester(System.out);
        if (tester.Initialize(args[0]))
            tester.ExecuteTests();
    }
}
