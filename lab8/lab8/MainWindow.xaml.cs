using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using MessageBox = System.Windows.MessageBox;

namespace lab8;

/*
 * Author: https://github.com/KanarekLife/PT
*/

/// <summary>
/// Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    private DirectoryInfo? _currentDirectory = null;

    public MainWindow()
    {
        InitializeComponent();
        FileListView.ContextMenu = new ContextMenu();
    }

    private void OpenFile_OnClick(object sender, RoutedEventArgs e)
    {
        var dlg = new FolderBrowserDialog() { Description = "Select directory to open" };
        var result = dlg.ShowDialog();

        if (result != System.Windows.Forms.DialogResult.OK)
        {
            return;
        }

        if (!Directory.Exists(dlg.SelectedPath))
        {
            MessageBox.Show(this, "Invalid path selected", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }

        _currentDirectory = new DirectoryInfo(dlg.SelectedPath);

        DisplayFiles();
    }

    private void DisplayFiles()
    {
        FileListView.Items.Clear();

        if (_currentDirectory is null)
        {
            return;
        }

        var subDirs = _currentDirectory.EnumerateDirectories().Select(GetTreeViewItem);
        var subFiles = _currentDirectory.EnumerateFiles().Select(GetTreeViewItem);
        foreach (var item in subDirs.Concat(subFiles))
        {
            FileListView.Items.Add(item);
        }

        FileListView.ContextMenu?.Items.Clear();
        var createMenuItem = new MenuItem
        {
            Header = "Create",
            Tag = _currentDirectory.FullName,
        };
        createMenuItem.Click += TreeViewDirectoryItem_OnCreate;
        FileListView.ContextMenu?.Items.Add(createMenuItem);
    }

    private void Exit_OnClick(object sender, RoutedEventArgs e)
    {
        System.Windows.Application.Current.Shutdown();
    }

    private TreeViewItem GetTreeViewItem(DirectoryInfo directoryInfo)
    {
        var subDirs = directoryInfo.EnumerateDirectories().Select(GetTreeViewItem);
        var subFiles = directoryInfo.EnumerateFiles().Select(GetTreeViewItem);

        var root = new TreeViewItem
        {
            Header = directoryInfo.Name,
            Tag = directoryInfo.FullName,
            ContextMenu = new ContextMenu()
        };

        var createMenuItem = new MenuItem
        {
            Header = "Create",
            Tag = directoryInfo.FullName,
        };
        createMenuItem.Click += TreeViewDirectoryItem_OnCreate;
        root.ContextMenu.Items.Add(createMenuItem);

        var deleteMenuItem = new MenuItem
        {
            Header = "Delete",
            Tag = directoryInfo.FullName
        };
        deleteMenuItem.Click += TreeViewDirectoryItem_OnDelete;
        root.ContextMenu.Items.Add(deleteMenuItem);

        foreach (var item in subDirs.Concat(subFiles))
        {
            root.Items.Add(item);
        }

        return root;
    }

    private TreeViewItem GetTreeViewItem(FileInfo fileInfo)
    {
        var item = new TreeViewItem
        {
            Header = fileInfo.Name,
            Tag = fileInfo.FullName,
            ContextMenu = new ContextMenu(),
        };
        item.Selected += TreeViewFileItem_OnSelected;
        item.MouseDoubleClick += TreeViewFileItem_OnDoubleClick;

        var openMenuItem = new MenuItem
        {
            Header = "Open",
            Tag = fileInfo.FullName
        };
        openMenuItem.Click += TreeViewFileItem_OnOpen;
        item.ContextMenu.Items.Add(openMenuItem);

        var deleteMenuItem = new MenuItem
        {
            Header = "Delete",
            Tag = fileInfo.FullName
        };
        deleteMenuItem.Click += TreeViewFileItem_OnDelete;
        item.ContextMenu.Items.Add(deleteMenuItem);

        return item;
    }

    private void TreeViewFileItem_OnDoubleClick(object sender, MouseButtonEventArgs e)
    {
        var item = e.Source as TreeViewItem;

        if (item?.Tag is not string path || !File.Exists(path))
        {
            MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        FileView.Text = File.ReadAllText(path, Encoding.UTF8);
    }

    private void TreeViewFileItem_OnSelected(object sender, RoutedEventArgs e)
    {
        var menuItem = e.Source as TreeViewItem;

        if (menuItem?.Tag is not string path || !File.Exists(path))
        {
            MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        var attributes = File.GetAttributes(path);
        var dosAttributes = new StringBuilder();
        dosAttributes.Append((attributes & FileAttributes.ReadOnly) != 0 ? 'r' : '-');
        dosAttributes.Append((attributes & FileAttributes.Archive) != 0 ? 'a' : '-');
        dosAttributes.Append((attributes & FileAttributes.System) != 0 ? 's' : '-');
        dosAttributes.Append((attributes & FileAttributes.Hidden) != 0 ? 'h' : '-');

        AttributeTextBlock.Text = dosAttributes.ToString();
    }

    private void TreeViewFileItem_OnOpen(object sender, RoutedEventArgs e)
    {
        var menuItem = e.Source as MenuItem;

        if (menuItem?.Tag is not string path || !File.Exists(path))
        {
            MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        FileView.Text = File.ReadAllText(path, Encoding.UTF8);
    }

    private void TreeViewDirectoryItem_OnCreate(object sender, RoutedEventArgs e)
    {
        var menuItem = e.Source as MenuItem;

        if (menuItem?.Tag is not string path || !Directory.Exists(path))
        {
            MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        var window = new CreateItemDialog(new DirectoryInfo(path));
        window.ShowDialog();
        DisplayFiles();
    }

    private void TreeViewDirectoryItem_OnDelete(object sender, RoutedEventArgs e)
    {
        var menuItem = e.Source as MenuItem;

        if (menuItem?.Tag is not string path || !Directory.Exists(path))
        {
            MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        foreach (var file in Directory.EnumerateFiles(path, "*", SearchOption.AllDirectories))
        {
            if (File.GetAttributes(file).HasFlag(FileAttributes.ReadOnly))
            {
                File.SetAttributes(file, File.GetAttributes(file) & ~FileAttributes.ReadOnly);
            }

            File.Delete(file);
        }
        Directory.Delete(path);
        DisplayFiles();
    }

    private void TreeViewFileItem_OnDelete(object sender, RoutedEventArgs e)
    {
        var menuItem = e.Source as MenuItem;

        if (menuItem?.Tag is not string path || !File.Exists(path))
        {
            MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            return;
        }

        if (File.GetAttributes(path).HasFlag(FileAttributes.ReadOnly))
        {
            File.SetAttributes(path, File.GetAttributes(path) & ~FileAttributes.ReadOnly);
        }

        File.Delete(path);
        DisplayFiles();
    }
}