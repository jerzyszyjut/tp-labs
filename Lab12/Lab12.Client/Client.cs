using System.Diagnostics;
using System.Net.Sockets;
using System.Net;
using System.Text.Json;
using Lab12.Model;

namespace Lab12.Client
{
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
                Debug.WriteLine("Error while connecting to server");
                return false;
            }
            Debug.WriteLine("Connected to server");

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
                    Debug.WriteLine($"Numerator changed from {model.Numerator} to {result.Numerator}");
                }
                if (result.Denominator != model.Denominator)
                {
                    Debug.WriteLine($"Denominator changed from {model.Denominator} to {model.Denominator}");
                }
                if (result.Result != model.Result)
                {
                    Debug.WriteLine($"Result changed from {model.Result} to {model.Result}");
                }
                if (!result.Name.Equals(model.Name))
                {
                    Debug.WriteLine($"Name changed from {model.Name} to {model.Name}");
                }
            }
            else
            {
                Debug.WriteLine("There was an error");
            }

            return result!;
        }

        public void Disconnect()
        {
            clientSocket.Close();
            Debug.WriteLine("Disconnected from server");
        }
    }

}

