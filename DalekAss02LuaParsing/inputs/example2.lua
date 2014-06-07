-- Lua reference @ http://www.lua.org/manual/5.2/manual.html#3.1
-- Lua Demo @ http://www.lua.org/cgi-bin/demo

function exampleFunction(param1, param2, param3)
    if param1 then
 	    io.write("\ncould be ") 
        io.write("something")
    else 
    	while param3 < 10 do 
    		io.write("looping ")
    		param3 = param3 + 1
    	end
    	io.write("\nor could be ")
        io.write("something else")
    end 
    io.write("\n\nAnd here's the ")
    io.write(param2)
end
exampleFunction(false, "parameter string", 0)