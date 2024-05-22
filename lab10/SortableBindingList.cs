using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab10;

public class SortableBindingList<T> : BindingList<T>
{
    private bool _isSorted;
    private ListSortDirection _sortDirection;
    private PropertyDescriptor _sortProperty;

    public SortableBindingList() : base() { }

    public SortableBindingList(IList<T> list) : base(list) { }

    protected override bool SupportsSortingCore => true;
    protected override bool IsSortedCore => _isSorted;
    protected override ListSortDirection SortDirectionCore => _sortDirection;
    protected override PropertyDescriptor SortPropertyCore => _sortProperty;

    protected override void ApplySortCore(PropertyDescriptor prop, ListSortDirection direction)
    {
        if (prop.PropertyType.GetInterface("IComparable") != null)
        {
            var items = this.Items as List<T>;
            items.Sort(new PropertyComparer<T>(prop, direction));
            _sortProperty = prop;
            _sortDirection = direction;
            _isSorted = true;
            this.OnListChanged(new ListChangedEventArgs(ListChangedType.Reset, -1));
        }
        else
        {
            _isSorted = false;
        }
    }

    protected override bool SupportsSearchingCore => true;

    protected override int FindCore(PropertyDescriptor prop, object key)
    {
        for (int i = 0; i < Count; i++)
        {
            if (prop.GetValue(this[i]).Equals(key))
                return i;
        }
        return -1;
    }

    public List<T> FindAll(string propertyName, object key)
    {
        PropertyDescriptor prop = TypeDescriptor.GetProperties(typeof(T)).Find(propertyName, true);
        if (prop == null)
            throw new ArgumentException("Invalid property name.", nameof(propertyName));

        var result = new List<T>();
        foreach (var item in this.Items)
        {
            if (prop.GetValue(item).Equals(key))
                result.Add(item);
        }
        return result;
    }
}

public class PropertyComparer<T> : IComparer<T>
{
    private PropertyDescriptor _property;
    private ListSortDirection _direction;

    public PropertyComparer(PropertyDescriptor property, ListSortDirection direction)
    {
        _property = property;
        _direction = direction;
    }

    public int Compare(T x, T y)
    {
        var value1 = _property.GetValue(x);
        var value2 = _property.GetValue(y);
        if (value1 is IComparable comparableValue1 && value2 != null)
        {
            return _direction == ListSortDirection.Ascending
                ? comparableValue1.CompareTo(value2)
                : ((IComparable)value2).CompareTo(value1);
        }
        return 0;
    }
}
