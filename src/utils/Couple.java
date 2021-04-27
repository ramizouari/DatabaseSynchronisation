package utils;

public class Couple<T1,T2>
{
    public T1 first;
    public T2 second;
    public Couple(T1 _1,T2 _2)
    {
        first=_1;
        second=_2;
    }
    @Override
    public String toString()
    {
        return "(%s,%s)".formatted(first,second);
    }
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Couple))
            return false;
        Couple<T1,T2> O=(Couple<T1,T2>) other;
        return first.equals(O.first) && second.equals(O.second);
    }
}
