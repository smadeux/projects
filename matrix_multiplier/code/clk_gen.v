`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 10/25/2018 01:23:02 PM
// Design Name: 
// Module Name: clk_gen
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module clk_gen(
    input clk,
    input rst,
    output clk_div
    );
    reg [32:0] count;
    
    initial
    begin
        count = 0;
    end
    
    assign clk_div = count[18];
    
    always@(posedge clk, posedge rst)
    begin
        if(rst)
            count <= 0; //Reset counter if rst is high
        else
            count <= count + 1; //Increment the counter
    end
endmodule
