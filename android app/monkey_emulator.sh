#!/bin/bash

#Polecam zostawić małą ilość ruchów dlatego ,że jeżeli ubijemy program małpka
#będzie chodziła dalej dopóki nie skończy tych ruchów.
#Więc lepiej mieć dużo testów niż więcej ruchów (tak mi się wydaje)
ILE_RUCHOW=1000

for n in {0..500}
do
	seed=$RANDOM
	pctTouch=$(expr $n % 101)
	echo "Start testu $n z parametrami: seed:$seed, pctTouch:$pctTouch"
	out=`./adb -e shell monkey -p jira.For.Android --kill-process-after-error --throttle 120 --pct-touch $pctTouch -s $seed $ILE_RUCHOW | tail -n 50`

	if [[ "$out" == *CRASH* ]]
	then
		echo "Mam errora!"
		time=$(date)
		echo -e "Bug report on $time from ADB Monkey with:\nseed: $seed\npct-touch: $pctTouch\nmoves: $ILE_RUCHOW\n\n\n$out" > $n.emulator_test
	fi
done




