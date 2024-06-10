using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Reflection;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using model;

namespace client;

public class Client
{
    private Socket clientSocket;

    public bool ConnectToServer()
    {
        try
        {
            clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            clientSocket.Connect(IPAddress.Loopback.ToString(), 8080);
        }
        catch
        {
            Console.WriteLine("Error while connecting to server");
            return false;
        }
        Console.WriteLine("Connected to server");

        return true;
    }

    public Model SendModelToServer(Model model)
    {
        var resultTask = Task.Run(() =>
        {
            NetworkStream networkStream = new(clientSocket);
            StreamWriter streamWrite = new StreamWriter(networkStream);
            streamWrite.WriteLine(JsonSerializer.Serialize(model));
            streamWrite.Flush();

            StreamReader streamRead = new StreamReader(networkStream);
            Model newModel = JsonSerializer.Deserialize<Model>(streamRead.ReadLine());

            streamWrite.Close();
            streamRead.Close();

            return newModel;
        });

        Task.WaitAll(resultTask);

        Model result = resultTask.Result;

        if (result != null)
        {
            if (result.Numerator != model.Numerator)
            {
                Console.WriteLine($"Numerator changed from {model.Numerator} to {result.Numerator}");
            }
            if (result.Denominator != model.Denominator)
            {
                Console.WriteLine($"Denominator changed from {model.Denominator} to {result.Denominator}");
            }
            if (result.Result != model.Result)
            {
                Console.WriteLine($"Result changed from {model.Result} to {result.Result}");
            }
            if (!result.Name.Equals(model.Name))
            {
                Console.WriteLine($"Name changed from {model.Name} to {result.Name}");
            }
        }
        else
        {
            Console.WriteLine("There was an error");
        }

        return result!;
    }

    public void Disconnect()
    {
        clientSocket.Close();
        Console.WriteLine("Disconnected from server");
    }
}
