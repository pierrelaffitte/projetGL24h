library(rpart)

# Données
data <- read.csv("resources/iris.csv")

# Taille de l'échantillon
n<-nrow(data) 
# Nombre de blocs
K<-10

# Tirer une permutation
alea=runif(n)
rang=rank(alea)
rang[1:10]

# Taille de chaque échantillon
taille<-n%/%K
taille

# Compo des blocs
block=(rang-1)%/%taille+1
table(block)

err.cv=numeric(0)
for(k in 1:K){
  test_rows = sample.int(nrow(data), nrow(data)/4)
  test = data[test_rows,]
  base = data[-test_rows,]
  CART=rpart(Species~.,data[block!=k,], method="class",
             parms=list( split='gini'))
  predCART=predict(CART,test,type="class")
  matCART=table(test$Species,predCART)
  taux_err_CART= sum(predCART!= test$Species)/nrow(test)
  err.cv=rbind(err.cv,taux_err_CART)
}
err.cv

err_final <- mean(err.cv)

print(err_final)