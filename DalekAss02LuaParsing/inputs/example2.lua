-- Lua reference manual @ http://www.lua.org/manual/5.2/manual.html#3.1
-- Lua demo @ http://www.lua.org/cgi-bin/demo

function exampleFunction(param1, param2, param3)
    if param1 then
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

exampleFunction(false, "cool String parameter.'", 0)