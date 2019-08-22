import ru.tellary.backenum.BackwardEnum;

enum Role {
    CREATOR, ASSIGNEE, FUTURE;

    public static class Backward extends BackwardEnum<Role> {
        public static final Backward CREATOR = new Backward(Role.CREATOR);
        public static final Backward ASSIGNEE = new Backward(Role.ASSIGNEE);

        public Backward(Role role) { super(role); }
        public Backward(String name) { super(Role.class, name); }
    }
}
