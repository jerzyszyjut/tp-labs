using System.ComponentModel;
using System.Diagnostics;
using System.IO.Compression;
using System.IO;
using System.Numerics;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace lab11;

/// <summary>
/// Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    private BackgroundWorker? backgroundWorker = null;
    delegate BigInteger Newton(Tuple<int, int> t);

    public MainWindow()
    {
        InitializeComponent();
        CalculateKandNTasks(10, 5);
        CalculateKandNDelegates(10, 5);
        CalculateKandNAsyncAwaitAsync(10, 5);
    }

    private void CalculateKandNTasks(int n, int k)
    {
        Task<BigInteger> numeratorTask = Task.Run(() =>
        {
            BigInteger result = 1;
            for (int i = 0; i <= k + 1; i++)
            {
                result *= (n - i);
            }
            return result;
        });
        Task<BigInteger> denominatorTask = Task.Run(() =>
        {
            BigInteger result = 1;
            for (int i = 2; i <= k; i++)
            {
                result *= i;
            }
            return result;
        });

        Task.WaitAll(numeratorTask, denominatorTask);

        var result = numeratorTask.Result / denominatorTask.Result;
        Debug.WriteLine($"1: {result}");
    }

    private void CalculateKandNDelegates(int n, int k)
    {
        Tuple<int, int> kAndN = new(n, k);

        Func<Tuple<int, int>, BigInteger> calculateNumerator = tuple =>
        {
            int n = tuple.Item1;
            int k = tuple.Item2;
            BigInteger result = 1;
            for (int i = 0; i <= k + 1; i++)
            {
                result *= (n - i);
            }
            return result;
        };
        Func<Tuple<int, int>, BigInteger> calculateDenominator = tuple =>
        {
            int k = tuple.Item2;
            BigInteger result = 1;
            for (var i = 2; i <= k; i++)
            {
                result *= i;
            }

            return result;
        };

        var numeratorDelegate = new Newton(calculateNumerator);
        var denominatorDelegate = new Newton(calculateDenominator);

        Task<BigInteger> numeratorTaskResult = Task.Run(() => numeratorDelegate(kAndN));
        Task<BigInteger> denominatorTaskResult = Task.Run(() => denominatorDelegate(kAndN));

        Task.WaitAll(numeratorTaskResult, denominatorTaskResult);

        BigInteger numerator = numeratorTaskResult.Result;
        BigInteger denominator = denominatorTaskResult.Result;

        var result = numerator / denominator;
        Debug.WriteLine($"2: {result}");
    }

    private async Task CalculateKandNAsyncAwaitAsync(int n, int k)
    {
        Tuple<int, int> kAndN = new(n, k);

        static async Task<BigInteger> calculateNumeratorAsync(int n, int k)
        {
            BigInteger result = 1;
            for (int i = 0; i <= k + 1; i++)
            {
                result *= (n - i);
            }
            return result;
        }

        static async Task<BigInteger> calculateDenominatorAsync(int k)
        {
            BigInteger result = 1;
            for (var i = 2; i <= k; i++)
            {
                result *= i;
            }

            return result;
        }

        var numeratorTask = Task.Run(() => calculateNumeratorAsync(n, k));
        var denominatorTask = Task.Run(() => calculateDenominatorAsync(k));

        await numeratorTask;
        await denominatorTask;

        var result = numeratorTask.Result / denominatorTask.Result;
        Debug.WriteLine($"3: {result}");
    }

    private void FibonacciButton_Click(object sender, RoutedEventArgs e)
    {
        FibonacciProgressBar.Value = 0;
        backgroundWorker = new BackgroundWorker();
        backgroundWorker.DoWork += (s, a) =>
        {
            var worker = s as BackgroundWorker;
            var n = (int)a.Argument;
            var results = new BigInteger[n];
            results[0] = 1;
            results[1] = 1;
            for (var i = 2; i < n; i++)
            {
                results[i] = results[i - 2] + results[i - 1];
                worker.ReportProgress((int)((double)(i + 1) / n * 100));
                Thread.Sleep(5);
            }

            a.Result = results[n - 1];
        };
        backgroundWorker.ProgressChanged += (o, args) =>
        {
            FibonacciProgressBar.Value = args.ProgressPercentage;
        };
        backgroundWorker.RunWorkerCompleted += (o, args) =>
        {
            FibonacciNumberOutput.Text = args.Result!.ToString()!;
        };
        backgroundWorker.WorkerReportsProgress = true;
        backgroundWorker.RunWorkerAsync(int.Parse(FibonacciNumberInput.Text));
    }

    private void Zip_Click(object sender, RoutedEventArgs e)
    {
        var dialog = new FolderBrowserDialog();

        if (dialog.ShowDialog() != System.Windows.Forms.DialogResult.OK)
        {
            return;
        }

        var dirInfo = new DirectoryInfo(dialog.SelectedPath);

        Parallel.ForEach(dirInfo.EnumerateFiles(), fileInfo =>
        {
            using var fs = fileInfo.OpenRead();
            using var os = File.Open(fileInfo.FullName + ".gz", FileMode.Create);
            using var gs = new GZipStream(os, CompressionMode.Compress);
            fs.CopyTo(gs);
        });
    }

    private void Unzip_Click(object sender, RoutedEventArgs e)
    {
        var dialog = new FolderBrowserDialog();

        if (dialog.ShowDialog() != System.Windows.Forms.DialogResult.OK)
        {
            return;
        }

        var dirInfo = new DirectoryInfo(dialog.SelectedPath);

        Parallel.ForEach(dirInfo.EnumerateFiles(), fileInfo =>
        {
            using var fs = fileInfo.OpenRead();
            using var os = File.Open(fileInfo.FullName.Replace(".gz", ""), FileMode.Create);
            using var gs = new GZipStream(fs, CompressionMode.Decompress);
            gs.CopyTo(os);
        });
    }
}