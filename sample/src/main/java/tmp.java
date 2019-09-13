public class tmp {
    public static void main(String[] args) {
        Role.Backward e = Role.Backward.CREATOR;
        switch(e.get()) {
        case CREATOR:
            System.out.println(Role.CREATOR);
        }
        e = new Role.Backward("abc");
        System.out.println(e.name());
        System.out.println(e.get());
    }
}
