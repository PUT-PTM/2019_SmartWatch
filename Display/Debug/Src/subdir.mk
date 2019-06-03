################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Src/epd1in54.c \
../Src/epdif.c \
../Src/epdpaint.c \
../Src/font12.c \
../Src/font16.c \
../Src/font20.c \
../Src/font24.c \
../Src/font8.c \
../Src/imagedata.c \
../Src/main.c \
../Src/stm32f4xx_hal_msp.c \
../Src/stm32f4xx_it.c \
../Src/syscalls.c \
../Src/system_stm32f4xx.c 

OBJS += \
./Src/epd1in54.o \
./Src/epdif.o \
./Src/epdpaint.o \
./Src/font12.o \
./Src/font16.o \
./Src/font20.o \
./Src/font24.o \
./Src/font8.o \
./Src/imagedata.o \
./Src/main.o \
./Src/stm32f4xx_hal_msp.o \
./Src/stm32f4xx_it.o \
./Src/syscalls.o \
./Src/system_stm32f4xx.o 

C_DEPS += \
./Src/epd1in54.d \
./Src/epdif.d \
./Src/epdpaint.d \
./Src/font12.d \
./Src/font16.d \
./Src/font20.d \
./Src/font24.d \
./Src/font8.d \
./Src/imagedata.d \
./Src/main.d \
./Src/stm32f4xx_hal_msp.d \
./Src/stm32f4xx_it.d \
./Src/syscalls.d \
./Src/system_stm32f4xx.d 


# Each subdirectory must supply rules for building sources it contributes
Src/%.o: ../Src/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo $(PWD)
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 '-D__weak=__attribute__((weak))' '-D__packed=__attribute__((__packed__))' -DUSE_HAL_DRIVER -DSTM32F407xx -I"C:/Users/Szymon/Desktop/bckp/Inc" -I"C:/Users/Szymon/Desktop/bckp/Drivers/STM32F4xx_HAL_Driver/Inc" -I"C:/Users/Szymon/Desktop/bckp/Drivers/STM32F4xx_HAL_Driver/Inc/Legacy" -I"C:/Users/Szymon/Desktop/bckp/Drivers/CMSIS/Device/ST/STM32F4xx/Include" -I"C:/Users/Szymon/Desktop/bckp/Drivers/CMSIS/Include"  -Og -g3 -Wall -fmessage-length=0 -ffunction-sections -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


