`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 11/29/2018 11:32:29 AM
// Module Name: bin2_BCD1dig
// Project Name: Matrix Multiplier
// Description: Module to determine what happend to each BCD digit as it is being
//              converted from binary to BCD
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module bin2BCD_1dig(
    input done,
    input d_in,
    input clk,
    output d_out,
    output reg [3:0] Q
    );
    
    parameter wait2strt = 2'b00;
    parameter shift = 2'b01;
    parameter add3_shift = 2'b10;
    
    reg [1:0] state, next_state;
    wire [3:0] next_data, next_data3;
    reg [3:0] D;
    wire GT4;
    
    assign GT4 = next_data > 4;
    assign next_data3 = next_data + 3;
    assign next_data = {Q[2:0], d_in};
    assign d_out = Q[3];
    
    always @ (state)
    case (state)
    wait2strt:      if (!done)              begin next_state = shift;         D = next_data;    end
                    else                    begin next_state = wait2strt; D = 4'b0000;          end
    shift:          if      (GT4 && !done)  begin next_state = add3_shift; D = next_data3;      end
                    else if (!GT4&& !done)  begin next_state = shift; D = next_data;            end
                    else                    begin next_state = wait2strt; D = 4'b0000;          end
    add3_shift:     if (GT4 && !done)       begin next_state = add3_shift; D = next_data3;      end
                    else if (!GT4 && !done) begin next_state = shift; D = next_data;            end
                    else                    begin next_state = wait2strt; D = 4'b0000;          end
    endcase
    
    initial state = 2'b00;
    always @ (posedge clk)
      state <= next_state;
      
    initial Q = 4'b0000;
    always @ (posedge clk)
      Q <= D;
      
endmodule