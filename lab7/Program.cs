using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;

namespace ConsoleApp
{
    static class Program
    {
        static void Main(string[] args)
        {
            if (args.Length == 0 || !Directory.Exists(args[0]))
            {
                Console.WriteLine("Podaj poprawną ścieżkę do katalogu jako parametr wywołania programu.");
                return;
            }

            string directoryPath = args[0];
            DirectoryInfo directory = new DirectoryInfo(directoryPath);

            DisplayDirectoryContentWithIndentation(directory);

            DateTime oldestFileDate = directory.GetOldestFileDate();
            Console.WriteLine($"\nNajstarszy plik: {oldestFileDate}");

            var sortedCollection = LoadDirectoryContentsToSortedCollection(directory);
            DisplaySortedCollection(sortedCollection);

            SerializeAndDeserializeCollection(sortedCollection);

            Console.ReadKey();
        }

        static void DisplayDirectoryContentWithIndentation(DirectoryInfo directory, int level = 0)
        {
            String indentation = "";
            for (int i = 0; i < level; i++) indentation += "\t";
            foreach (var file in directory.GetFiles())
            {
                Console.WriteLine($"{indentation}{file.Name} {file.Length} bajtów {GetDOSAttributes(file)}");
            }
            foreach (var subDirectory in directory.GetDirectories())
            {
                Console.WriteLine($"{indentation}{subDirectory.Name} ({subDirectory.GetFiles("*.*", SearchOption.AllDirectories).Length}) {GetDOSAttributes(subDirectory)}");
                DisplayDirectoryContentWithIndentation(subDirectory, level + 1);
            }
        }

        static DateTime GetOldestFileDate(this DirectoryInfo directory)
        {
            return directory.GetFiles("*.*", SearchOption.AllDirectories)
                .Select(file => file.LastWriteTime)
                .OrderBy(date => date)
                .FirstOrDefault();
        }

        static string GetDOSAttributes(FileSystemInfo fileSystemInfo)
        {
            string attributes = "";
            if ((fileSystemInfo.Attributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                attributes += "r";
            else
                attributes += "-";
            if ((fileSystemInfo.Attributes & FileAttributes.Archive) == FileAttributes.Archive)
                attributes += "a";
            else
                attributes += "-";
            if ((fileSystemInfo.Attributes & FileAttributes.Hidden) == FileAttributes.Hidden)
                attributes += "h";
            else
                attributes += "-";
            if ((fileSystemInfo.Attributes & FileAttributes.System) == FileAttributes.System)
                attributes += "s";
            else
                attributes += "-";
            return attributes;
        }

        static void DisplaySortedCollection(SortedDictionary<string, long> collection)
        {
            foreach (var item in collection)
            {
                Console.WriteLine($"{item.Key} -> {item.Value}");
            }
        }

        static SortedDictionary<string, long> LoadDirectoryContentsToSortedCollection(DirectoryInfo directory)
        {
            var comparer = new FilesComparer();
            var sortedCollection = new SortedDictionary<string, long>(comparer);
            foreach (var file in directory.GetFiles())
            {
                sortedCollection.Add(file.Name, file.Length);
            }
            foreach (var subDirectory in directory.GetDirectories())
            {
                sortedCollection.Add(subDirectory.Name, subDirectory.GetFiles().Length);
            }
            return sortedCollection;
        }

        [Serializable]
        class FilesComparer : IComparer<string>
        {
            public int Compare(string x, string y)
            {
                return x.CompareTo(y);
            }
        }

        static void SerializeAndDeserializeCollection(SortedDictionary<string, long> collection)
        {
            BinaryFormatter formatter = new BinaryFormatter();
            using (FileStream fileStream = new FileStream("collection.bin", FileMode.Create))
            {
                formatter.Serialize(fileStream, collection);
            }
            using (FileStream fileStream = new FileStream("collection.bin", FileMode.Open))
            {
                var deserializedCollection = (SortedDictionary<string, long>)formatter.Deserialize(fileStream);
                Console.WriteLine("\nDeserializowana kolekcja:");
                DisplaySortedCollection(deserializedCollection);
            }
        }
    }
}
