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

namespace server;

public class Server
{
    private readonly Dictionary<int, TcpClient> clients = new();

    public void Start()
    {
        var listener = new TcpListener(IPAddress.Any, 8080);
        listener.Start();

        Console.WriteLine("Server is listening for connections on port 8080");

        while (true)
        {
            Socket clientSocket = listener.AcceptSocket();
            

            ClientHandler handler = new(clientSocket);
            Thread clientThread = new(handler.HandleClient);
            clientThread.IsBackground = true;
            clientThread.Start();
        }
    }
}

public class ClientHandler
{
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void HandleClient()
    {
        NetworkStream networkStream = new(clientSocket);
        StreamReader streamReader = new(networkStream);
        StreamWriter streamWriter = new(networkStream);
        string data = streamReader.ReadLine();
        Console.WriteLine($"Received: {data}");
        Model model = JsonSerializer.Deserialize<Model>(data);

        Model newModel = new()
        {
            Result = model.Numerator / model.Denominator,
            Numerator = model.Numerator,
            Denominator = model.Denominator,
            Name = model.Name + " d-_-b "
        };

        Thread.Sleep(10000);

        string newModelString = JsonSerializer.Serialize(newModel);
        streamWriter.WriteLine(newModelString);
        streamWriter.Flush();
        Console.WriteLine($"Sent: {newModelString}");

        streamWriter.Close();
        streamReader.Close();
        networkStream.Close();
    }
}
