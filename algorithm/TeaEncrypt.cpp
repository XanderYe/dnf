#include <string.h>
#include <stdio.h>
char *key = "troqkddmtroqkcdm";
char *postfix = "e8b10c1f8bc3595be8b10c1f8bc3595b";
class Encrypt {
	public:
	void getKeyPre(char sixkey[9],char *ret) {
		ret[0] = 0;
		char res1[80];
		encrypt(sixkey, res1);
		strcat(ret, res1);
		strcat(ret, postfix);
	}
	private:
	_int64 bytes_to_long(char *t) {
		unsigned char *r = (unsigned char *)t;
		_int64 a = (0xFFFFFFFF & (unsigned int)r[0]) << 24;
		_int64 b = (0xFFFFFFFF & (unsigned int)r[1]) << 16;
		_int64 c = (0xFFFFFFFF & (unsigned int)r[2]) << 8;
		_int64 d = r[3];
		return a + b + c + d;
	}
	void long_to_bytes(_int64 v, char *ret) {
		int a = (int)((0xFF000000 & v) >> 24);
		int b = (int)((0xFF0000 & v) >> 16);
		int c = (int)((0xFF00 & v) >> 8);
		int d = (int)(0xFF & v);
		sprintf(ret, "%02x%02x%02x%02x", a, b, c, d);
	}
	_int64 unpack(char* tmp,int start,int len) {
		unsigned char *arr = (unsigned char *)(tmp + start);
		_int64 d = arr[0];
		_int64 c = arr[1];
		_int64 b = arr[2];
		_int64 a = arr[3];
		a = a << 24;
		b = b << 16;
		c = c << 8;
		return a + b + c + d;
	}
	void encrypt(char v[9], char*ret) {
		_int64 v0 = bytes_to_long(v);
		_int64 v1 = bytes_to_long(v + 4);
		_int64 sum = 0;
		for (int i = 0; i < 32; ++i) {
			_int64 tv1 = toUInt32(toUInt32(v1 << 4)) ^ toUInt32((v1 >> 5 & 0x07FFFFFF));
			_int64 tv2 = unpack(key, ((int)sum & 3) * 4, 4);
			v0 = toUInt32(v0 + (toUInt32(tv1 + v1) ^ toUInt32(tv2 + sum)));
			sum = toUInt32(sum + 0x9E3779B9);
			tv1 = toUInt32(toUInt32(toUInt32(v0 << 4)) ^ toUInt32((v0 >> 5 & 0x07FFFFFF)));
			tv2 = unpack(key, ((int)toUInt32((int)sum >> 11) & 3) * 4, 4);
			v1 = toUInt32(v1 + (toUInt32(tv1 + v0) ^ toUInt32(tv2 + sum)));
		}
		long_to_bytes(v0, ret);
		long_to_bytes(v1, ret + 8);
	}
	_int64 toUInt32(_int64 v) {
		return v & 0xFFFFFFFF;
	}
}
;
void getKey(char sixkey[9],char *ret) {
	Encrypt enc;
	enc.getKeyPre(sixkey, ret);
}
int main() {
	char password[9] = "uu5!^%jg";
	char res[100];
	getKey(password,res);
	printf("%s",res);
	return 0;
}