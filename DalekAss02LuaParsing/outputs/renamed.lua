

function changedFunctionName(changedParam1, param2, param3)
    if changedParam1 then 
        io.write("\nIf case reporting in. ")
        io.write("Sorry, no loop. ")
    else 
        io.write("l")
        while param3 < 10 do 
            io.write(" o ")
            param3 = param3 + 1
        end 
        io.write("p\nElse case... ")
        io.write("End of loop.")
    end 
    io.write("\n\nAnd here's the '")
    io.write(param2)
end 

changedFunctionName(false, "cool String parameter.'", 0)
