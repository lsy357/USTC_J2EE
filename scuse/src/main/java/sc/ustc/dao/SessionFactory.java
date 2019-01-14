package sc.ustc.dao;

public class SessionFactory {

    protected static ThreadLocal<Session> tl = new ThreadLocal<Session>();

    /**
     * 将session与线程绑定
     *
     * @return
     */
    public static Session getCurrentSession() {
        Session session = tl.get();
        if (session == null) {
            session = new Session();
            tl.set(session);
        }
        return session;
    }

}
